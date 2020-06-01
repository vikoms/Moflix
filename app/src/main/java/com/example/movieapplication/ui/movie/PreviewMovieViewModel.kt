package com.example.movieapplication.ui.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.BuildConfig
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.entity.Movie
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PreviewMovieViewModel : ViewModel() {

    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY
        private const val urlImage = "https://image.tmdb.org/t/p/w154/"
        private const val urlBackdrop = "https://image.tmdb.org/t/p/w300/"
    }

    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)


    private val popularMovies = MutableLiveData<ArrayList<Movie>>()
    private val nowPlayingMovies = MutableLiveData<ArrayList<Movie>>()
    private val upcomingMovies = MutableLiveData<ArrayList<Movie>>()
    private val releaseNowMovies = MutableLiveData<ArrayList<Movie>>()

    fun setPopularMovies() {
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] =
            API_KEY
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"

        val movies = ArrayList<Movie>()
        val call = apiHelper.getPopularMovie(queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure ", "onFailure : ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItem = Movie()
                        movieItem.id = movie.getInt("id")
                        movieItem.title = movie.getString("title")
                        movieItem.poster = "$urlImage${movie.getString("poster_path")}"
                        movieItem.backdrop = "$urlBackdrop${movie.getString("backdrop_path")}"
                        movieItem.releaseDate = movie.getString("release_date")
                        movieItem.rating = movie.getDouble("vote_average")

                        movies.add(movieItem)
                    }

                    popularMovies.postValue(movies)

                } else {
                    popularMovies.postValue(null)
                }
            }

        })
    }

    fun setNowPlayingMovies() {
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] =
            API_KEY
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"

        val movies = ArrayList<Movie>()
        val call = apiHelper.getNowPlayingMovie(queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure ", "onFailure : ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItem = Movie()
                        movieItem.id = movie.getInt("id")
                        movieItem.title = movie.getString("title")
                        movieItem.poster = "$urlImage${movie.getString("poster_path")}"
                        movieItem.backdrop = "$urlBackdrop${movie.getString("backdrop_path")}"
                        movieItem.releaseDate = movie.getString("release_date")
                        movieItem.rating = movie.getDouble("vote_average")

                        movies.add(movieItem)
                    }

                    nowPlayingMovies.postValue(movies)

                } else {
                    nowPlayingMovies.postValue(null)
                }
            }

        })
    }

    fun setUpcomingMovies() {
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] =
            API_KEY
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"

        val movies = ArrayList<Movie>()
        val call = apiHelper.getUpcomingMovie(queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure ", "onFailure : ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItem = Movie()
                        movieItem.id = movie.getInt("id")
                        movieItem.title = movie.getString("title")
                        movieItem.poster = "$urlImage${movie.getString("poster_path")}"
                        movieItem.backdrop = "$urlBackdrop${movie.getString("backdrop_path")}"
                        movieItem.releaseDate = movie.getString("release_date")
                        movieItem.rating = movie.getDouble("vote_average")

                        movies.add(movieItem)
                    }

                    upcomingMovies.postValue(movies)

                } else {
                    upcomingMovies.postValue(null)
                }
            }

        })
    }

    fun setReleaseNowMovie() {

        val movies = ArrayList<Movie>()
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
                        val responseObject = JSONObject(response.body()?.string())
                        val list = responseObject.getJSONArray("results")
                        for (i in 0 until list.length()) {
                            val movie = list.getJSONObject(i)
                            val movieItem = Movie()
                            with(movieItem) {
                                id = movie.getInt("id")
                                title = movie.getString("title")
                                poster = "$urlImage${movie.getString("poster_path")}"
                                backdrop = "$urlBackdrop${movie.getString("backdrop_path")}"
                                releaseDate = movie.getString("release_date")
                                rating = movie.getDouble("vote_average")
                            }
                            movies.add(movieItem)
                        }
                        releaseNowMovies.postValue(movies)
                    } else {
                        releaseNowMovies.postValue(movies)
                        Log.d("failed notif", "${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    Log.d("failed notif", "${e.message}")
                    e.printStackTrace()
                }
            }

        })
    }


    fun getDataPopularMovie(): LiveData<ArrayList<Movie>> = popularMovies
    fun getDataNowPlayingMovie(): LiveData<ArrayList<Movie>> = nowPlayingMovies
    fun getDataUpcomingMovie(): LiveData<ArrayList<Movie>> = upcomingMovies
    fun getDataReleaseNowMovie(): LiveData<ArrayList<Movie>> = releaseNowMovies


}