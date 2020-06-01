package com.example.movieapplication.broadcast

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.movieapplication.BuildConfig
import com.example.movieapplication.R
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.ui.movie.viewmoremovie.ViewMoreMovieActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ReleaseReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        private const val ID_RELEASE = 100
        private const val ID_REPEATING = 101
        private const val TIME_FORMAT = "HH:mm"
    }


    override fun onReceive(context: Context, intent: Intent) {
        val title = context.resources.getString(R.string.text_new_movie)
        getMovieReleaseNow(context, title)
    }

    private fun getMovieReleaseNow(context: Context, title: String) {
        val dateFormat = "yyyy-MM-dd"
        val df = SimpleDateFormat(dateFormat, Locale.getDefault())
        val today = Calendar.getInstance().time
        val date = df.format(today)
        val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
        val map = HashMap<String, String>()
        map["api_key"] = BuildConfig.TMDB_API_KEY
        map["language"] = "en-US"
        map["sort_by"] = "primary_release_date.asc"
        map["page"] = "1"
        map["primary_release_date.gte"] = date
        map["primary_release_date.lte"] = date
        val call = apiHelper.getMovieReleaseNow(map)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onfailure notif", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val jsonObject = JSONObject(response.body()?.string())
                        val list = jsonObject.getJSONArray("results")
                        val message = String.format(
                            context.resources.getString(R.string.text_notif_new_movie),
                            "${list.length()}"
                        )
                        showNotification(context, title, message)
                    } else {

                        Log.d("failed notif", "${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    Log.d("failed notif", "${e.message}")
                    e.printStackTrace()
                }
            }

        })
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val CHANNEL_ID = "channel_01"
        val CHANNEL_NAME = "release reminder channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, ViewMoreMovieActivity::class.java)
        intent.putExtra(ViewMoreMovieActivity.EXTRA_TYPE_MOVIE, ViewMoreMovieActivity.RELEASE_NOW)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_movie)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_RELEASE, notification)
    }

    private fun showToast(context: Context, title: String, message: String) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_SHORT).show()
    }

    fun setReleaseReminderAlarm(context: Context, time: String) {
        if (isTimeInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseReminderReceiver::class.java)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(
            context,
            context.resources.getString(R.string.setup_alarm_release),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(
            context,
            context.resources.getString(R.string.release_reminder_cancel),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun isTimeInvalid(time: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(time)
            false
        } catch (e: ParseException) {
            e.printStackTrace()
            true
        }
    }
}
