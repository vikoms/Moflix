package com.example.movieapplication.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.BuildConfig
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.entity.Movie
import com.example.movieapplication.entity.Series
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val apiKey = BuildConfig.TMDB_API_KEY
    private val urlImage = "https://image.tmdb.org/t/p/w154/"
    private val urlBackdrop = "https://image.tmdb.org/t/p/w500/"

    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
    private val movies = MutableLiveData<ArrayList<Movie>>()
    private val series = MutableLiveData<ArrayList<Series>>()

    fun searchMovie(querySearch: String) {
        val movieList = ArrayList<Movie>()
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] = apiKey
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"
        queryMap["query"] = querySearch
        val call = apiHelper.getSearchMovieAndSeries("movie", queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onfailure search movie", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val jsonObject = JSONObject(response.body()?.string())
                        val list = jsonObject.getJSONArray("results")
                        for (i in 0 until list.length()) {
                            val movie = list.getJSONObject(i)
                            val movieItem = Movie()
                            with(movieItem) {
                                id = movie.getInt("id")
                                title = movie.getString("title")
                                releaseDate = movie.getString("release_date") ?: null
                                rating = movie.getDouble("vote_average")
                                poster = "$urlImage${movie.getString("poster_path")}"
                                backdrop = "$urlImage${movie.getString("backdrop_path")}"
                            }
                            movieList.add(movieItem)
                        }
                        movies.postValue(movieList)
                    } else {
                        movies.postValue(movieList)
                        Log.d("failed search movie", "${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("exception search movie", e.message)
                }
            }

        })
    }

    fun searchSeries(querySearch: String) {
        val seriesList = ArrayList<Series>()
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] = apiKey
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"
        queryMap["query"] = querySearch
        val call = apiHelper.getSearchMovieAndSeries("tv", queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onfailure search series", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val jsonObject = JSONObject(response.body()?.string())
                        val list = jsonObject.getJSONArray("results")
                        for (i in 0 until list.length()) {

                            val series = list.getJSONObject(i)
                            Log.d("berhasil1", "${series.getString("name")}")
                            val seriesitem = Series()
                            with(seriesitem) {
                                id = series.getInt("id")
                                title = series.getString("name")
                                releaseDate = series.getString("first_air_date")
                                rating = series.getDouble("vote_average")
                                poster = "$urlImage${series.getString("poster_path")}"
                                backdrop = "$urlImage${series.getString("backdrop_path")}"
                            }

                            seriesList.add(seriesitem)
                        }
                        Log.d("berhasil2", "${seriesList.size}")
                        series.postValue(seriesList)
                    } else {
                        series.postValue(seriesList)
                        Log.d("failed search series", "${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    Log.d("exception search series", e.message)
                    e.printStackTrace()
                }
            }

        })
    }

    fun getSearchMovie(): LiveData<ArrayList<Movie>> = movies
    fun getSearchSeries(): LiveData<ArrayList<Series>> = series

}