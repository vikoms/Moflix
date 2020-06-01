package com.example.movieapplication.ui.cast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.BuildConfig
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.entity.People
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CastViewModel : ViewModel() {

    private val apiKey = BuildConfig.TMDB_API_KEY
    private val urlImage = "https://image.tmdb.org/t/p/w154/"
    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
    private val cast = MutableLiveData<ArrayList<People>>()

    private lateinit var id: String
    private lateinit var type: String

    fun setData(id: String, type: String) {
        this.id = id
        this.type = type
    }


    fun setCast() {
        val castList = ArrayList<People>()
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] = apiKey
        val call = apiHelper.getCast(type, id, queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure Cast", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("cast")
                    for (i in 0 until list.length()) {
                        val cast = list.getJSONObject(i)
                        val castItem = People()
                        with(castItem) {
                            profilePath =
                                String.format("$urlImage%s", cast.getString("profile_path"))
                            name = cast.getString("name")
                            this.id = cast.getInt("id")
                            charCredits = cast.getString("character")
                        }
                        castList.add(castItem)
                    }
                    cast.postValue(castList)
                } else {
                    cast.postValue(castList)
                    Log.d("castFailed", "${response.errorBody()}")
                }
            }

        })
    }

    fun getCast(): LiveData<ArrayList<People>> = cast
}