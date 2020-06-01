package com.example.movieapplication.ui.detailmovie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.BuildConfig
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.entity.Genre
import com.example.movieapplication.entity.Movie
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel : ViewModel() {

    private val API_KEY = BuildConfig.TMDB_API_KEY
    private val urlImage = "https://image.tmdb.org/t/p/w154/"
    private val urlBackdrop = "https://image.tmdb.org/t/p/w500/"
    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
    private val movie = MutableLiveData<Movie>()

    fun setMovieDetail(id: String) {
        val movieGenres = ArrayList<Genre>()
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] = API_KEY
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"
        val call = apiHelper.getDetailMovie(id, queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure movie detail", "onFailure : ${t.message}")
                movie.postValue(null)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonObject = JSONObject(response.body()?.string())
                    val movieItem = Movie()
                    movieItem.id = jsonObject.getInt("id")
                    movieItem.title = jsonObject.getString("title")
                    movieItem.poster = "${urlImage}${jsonObject.getString("poster_path")}"
                    movieItem.backdrop = "${urlBackdrop}${jsonObject.getString("backdrop_path")}"
                    Log.d("backdrop", "${urlBackdrop}${jsonObject.getString("backdrop_path")}")
                    movieItem.releaseDate = jsonObject.getString("release_date")
                    movieItem.rating = jsonObject.getDouble("vote_average")
                    movieItem.runtime = jsonObject.getString("runtime")
                    movieItem.description = jsonObject.getString("overview")
                    val genres = jsonObject.getJSONArray("genres")
                    for (i in 0 until genres.length()) {
                        val genreObject = genres.getJSONObject(i)
                        movieGenres.add(Genre(genreObject.getString("name")))
                        movieItem.genres = movieGenres
                    }
                    movie.postValue(movieItem)
                } else {
                    movie.postValue(null)
                }
            }

        })
    }

    fun getDetailMovie(): LiveData<Movie> = movie
}