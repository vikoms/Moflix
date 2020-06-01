package com.example.movieapplication.ui.series

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.BuildConfig
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.entity.Series
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreviewSeriesViewModel : ViewModel() {

    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY
        private const val urlImage = "https://image.tmdb.org/t/p/w154/"
        private const val urlBackdrop = "https://image.tmdb.org/t/p/w300/"
    }

    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
    private val popularSeries = MutableLiveData<ArrayList<Series>>()
    private val airingTodaySeries = MutableLiveData<ArrayList<Series>>()
    private val ongoingSeries = MutableLiveData<ArrayList<Series>>()

    fun setPopularSeries() {
        val seriesList = ArrayList<Series>()
        val queries = HashMap<String, String>()
        queries["api_key"] = API_KEY
        queries["language"] = "en-US"
        queries["page"] = "1"

        val call = apiHelper.getPopularSeries(queries)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure", "onFailure : ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val series = list.getJSONObject(i)
                        val seriesItem = Series()
                        seriesItem.id = series.getInt("id")
                        seriesItem.title = series.getString("name")
                        seriesItem.poster = "${urlImage}${series.getString("poster_path")}"
                        seriesItem.backdrop = "${urlBackdrop}${series.getString("backdrop_path")}"
                        seriesItem.releaseDate = series.getString("first_air_date")
                        seriesItem.rating = series.getDouble("vote_average")
                        seriesList.add(seriesItem)
                    }
                    popularSeries.postValue(seriesList)
                } else {
                    popularSeries.postValue(null)
                }
            }

        })

    }

    fun setAiringTodaySeries() {
        val seriesList = ArrayList<Series>()
        val queries = HashMap<String, String>()
        queries["api_key"] = API_KEY
        queries["language"] = "en-US"
        queries["page"] = "1"

        val call = apiHelper.getAiringTodaySeries(queries)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure", "onFailure : ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val series = list.getJSONObject(i)
                        val seriesItem = Series()
                        seriesItem.id = series.getInt("id")
                        seriesItem.title = series.getString("name")
                        seriesItem.poster = "${urlImage}${series.getString("poster_path")}"
                        seriesItem.backdrop = "${urlBackdrop}${series.getString("backdrop_path")}"
                        seriesItem.releaseDate = series.getString("first_air_date")
                        seriesItem.rating = series.getDouble("vote_average")
                        seriesList.add(seriesItem)
                    }
                    airingTodaySeries.postValue(seriesList)
                } else {
                    airingTodaySeries.postValue(null)
                }
            }

        })

    }

    fun setOngoingSeries() {
        val seriesList = ArrayList<Series>()
        val queries = HashMap<String, String>()
        queries["api_key"] = API_KEY
        queries["language"] = "en-US"
        queries["page"] = "1"

        val call = apiHelper.getOnTvSeries(queries)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure", "onFailure : ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val series = list.getJSONObject(i)
                        val seriesItem = Series()
                        seriesItem.id = series.getInt("id")
                        seriesItem.title = series.getString("name")
                        seriesItem.poster = "${urlImage}${series.getString("poster_path")}"
                        seriesItem.backdrop = "${urlBackdrop}${series.getString("backdrop_path")}"
                        seriesItem.releaseDate = series.getString("first_air_date")
                        seriesItem.rating = series.getDouble("vote_average")
                        seriesList.add(seriesItem)
                    }
                    ongoingSeries.postValue(seriesList)
                } else {
                    ongoingSeries.postValue(null)
                }
            }

        })

    }

    fun getPopularSeries(): LiveData<ArrayList<Series>> = popularSeries
    fun getAiringTodaySeries(): LiveData<ArrayList<Series>> = airingTodaySeries
    fun getOngoingSeries(): LiveData<ArrayList<Series>> = ongoingSeries
}