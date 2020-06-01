package com.example.movieapplication.ui.detailseries

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.BuildConfig
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.entity.Genre
import com.example.movieapplication.entity.Season
import com.example.movieapplication.entity.Series
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSeriesViewModel : ViewModel() {

    private val apiKey = BuildConfig.TMDB_API_KEY
    private val urlImage = "https://image.tmdb.org/t/p/w154/"
    private val urlBackdrop = "https://image.tmdb.org/t/p/w500/"
    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
    private val series = MutableLiveData<Series>()

    fun setSeriesDetail(id: String) {
        val genres = ArrayList<Genre>()
        val seasons = ArrayList<Season>()
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] = apiKey
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"
        val call = apiHelper.getDetailSeries(id, queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure detail series", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseObject = JSONObject(response.body()?.string())
                    val seriesItem = Series()
                    with(seriesItem) {
                        title = responseObject.getString("name")
                        backdrop = "${urlBackdrop}${responseObject.getString("backdrop_path")}"
                        poster = "${urlImage}${responseObject.getString("poster_path")}"
                        description = responseObject.getString("overview")
                        releaseDate = responseObject.getString("first_air_date")
                        rating = responseObject.getDouble("vote_average")
                        this.id = responseObject.getInt("id")
                        episodeTotal = responseObject.getInt("number_of_episodes")
                        val genreArray = responseObject.getJSONArray("genres")
                        for (i in 0 until genreArray.length()) {
                            val genreObject = genreArray.getJSONObject(i)
                            genres.add(Genre(genreObject.getString("name")))
                            this.genres = genres
                        }

                        val seasonArray = responseObject.getJSONArray("seasons")
                        for (i in 0 until seasonArray.length()) {
                            val seasonObject = seasonArray.getJSONObject(i)
                            val season = Season()
                            with(season) {
                                name = seasonObject.getString("name")
                                this.id = seasonObject.getInt("id")
                                releaseDate = seasonObject.getString("air_date")
                                poster = "${urlImage}${seasonObject.getString("poster_path")}"
                                episodeCount = seasonObject.getInt("episode_count")
                                seasonNumber = seasonObject.getInt("season_number")
                                overview = seasonObject.getString("overview")
                            }
                            seasons.add(season)
                            this.seasons = seasons
                        }
                    }
                    series.postValue(seriesItem)
                } else {
                    series.postValue(null)
                    Log.d("failed detail movie", "${response.message()}")
                }
            }

        })
    }

    fun getDetailSeries(): LiveData<Series> = series
}