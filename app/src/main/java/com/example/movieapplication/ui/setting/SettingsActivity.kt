package com.example.movieapplication.ui.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapplication.R
import com.example.movieapplication.broadcast.DailyReminderReceiver
import com.example.movieapplication.broadcast.ReleaseReminderReceiver
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "reminder_status"
        private const val PREFS_DAILY = "DAILY"
        private const val PREFS_RELEASE = "RELEASE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.text_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        checkActive()

        switch_release.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                val releaseReminder = ReleaseReminderReceiver()
                if (checked) {
                    val time = "08:00"
                    releaseReminder.setReleaseReminderAlarm(this@SettingsActivity, time)
                    setActive(true, PREFS_RELEASE)
                } else {
                    releaseReminder.cancelAlarm(this@SettingsActivity)
                    setActive(false, PREFS_RELEASE)
                }
            }
        })

        switch_daily.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                val dailyReminder = DailyReminderReceiver()
                if (isChecked) {
                    val message = resources.getString(R.string.text_daily_reminder)
                    val time = "07:00"
                    dailyReminder.setDailyReminderAlarm(this@SettingsActivity, time, message)
                    setActive(true, PREFS_DAILY)
                } else {
                    dailyReminder.cancelAlarm(this@SettingsActivity)
                    setActive(false, PREFS_DAILY)
                }
            }

        })

        tv_setting.setOnClickListener {
            val changeLanguage = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(changeLanguage)
        }


    }

    private fun setActive(status: Boolean, type: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(type, status)
        editor.apply()
    }

    private fun checkActive() {
        val statusDaily = sharedPreferences.getBoolean(PREFS_DAILY, false)
        switch_daily.isChecked = statusDaily

        val statusRelease = sharedPreferences.getBoolean(PREFS_RELEASE, false)
        switch_release.isChecked = statusRelease
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
