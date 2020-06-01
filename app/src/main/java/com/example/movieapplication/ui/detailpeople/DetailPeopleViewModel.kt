package com.example.movieapplication.ui.detailpeople

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.BuildConfig
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.entity.Movie
import com.example.movieapplication.entity.People
import com.example.movieapplication.entity.Series
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPeopleViewModel : ViewModel() {

    private val apiKey = BuildConfig.TMDB_API_KEY
    private val urlImage = "https://image.tmdb.org/t/p/w154/"

    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
    private val people = MutableLiveData<People>()
    private val movies = MutableLiveData<ArrayList<Movie>>()
    private val series = MutableLiveData<ArrayList<Series>>()

    private lateinit var id: String

    fun setData(id: String) {
        this.id = id
    }

    fun setPeople() {
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] = apiKey
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"
        val call = apiHelper.getDetailPeople(id, queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure people detail", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val responseObject = JSONObject(response.body()?.string())
                        val peopleItem = People()
                        with(peopleItem) {
                            name = responseObject.getString("name")
                            birthday = responseObject.getString("birthday")
                            departement = responseObject.getString("known_for_department")
                            id = responseObject.getInt("id")
                            gender =
                                if (responseObject.getInt("gender") == 1) "Female" else if (responseObject.getInt(
                                        "gender"
                                    ) == 2
                                ) "Male" else null
                            profilePath = "$urlImage${responseObject.getString("profile_path")}"
                            biography = responseObject.getString("biography")
                            placeOfBirth = responseObject.getString("place_of_birth")
                        }
                        people.postValue(peopleItem)

                    } else {
                        Log.d("failed people detail", "${response.errorBody()}")
                        people.postValue(null)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("failed people detail", "${e.message}")
                }
            }

        })
    }

    fun setMovies() {
        val queryMap = HashMap<String, String>()
        val movieList = ArrayList<Movie>()
        queryMap["api_key"] = apiKey
        queryMap["language"] = "en-US"
        val call = apiHelper.getMovieCreditsPeople(id, queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure people movies", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val jsonObject = JSONObject(response.body()?.string())
                        val list = jsonObject.getJSONArray("cast")
                        for (i in 0 until list.length()) {
                            val movie = list.getJSONObject(i)
                            val movieItem = Movie()
                            with(movieItem) {
                                title = movie.getString("title")
                                releaseDate = movie.getString("release_date")
                                id = movie.getInt("id")
                                poster = "$urlImage${movie.getString("poster_path")}"
                            }
                            movieList.add(movieItem)
                        }
                        movies.postValue(movieList)
                    } else {
                        movies.postValue(movieList)
                        Log.d("failed people movies", "${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("failed people movies", "${e.message}")
                }
            }

        })
    }

    fun setSeries() {
        val queryMap = HashMap<String, String>()
        val seriesList = ArrayList<Series>()
        queryMap["api_key"] = apiKey
        queryMap["language"] = "en-US"
        val call = apiHelper.getSeriesCreditsPeople(id, queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure people series", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val jsonObject = JSONObject(response.body()?.string())
                        val list = jsonObject.getJSONArray("cast")
                        for (i in 0 until list.length()) {
                            val series = list.getJSONObject(i)
                            val seriesItem = Series()
                            with(seriesItem) {
                                title = series.getString("name")
                                id = series.getInt("id")
                                poster = "$urlImage${series.getString("poster_path")}"
                                releaseDate = series.getString("first_air_date")
                            }
                            seriesList.add(seriesItem)
                        }
                        series.postValue(seriesList)
                    } else {
                        series.postValue(seriesList)
                        Log.d("failed people series", "${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("failed people series", "${e.message}")
                }
            }

        })
    }

    fun getData(): LiveData<People> = people
    fun getDataMovies(): LiveData<ArrayList<Movie>> = movies
    fun getDataSeries(): LiveData<ArrayList<Series>> = series
}