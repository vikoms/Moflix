package com.example.movieapplication.ui.people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.api.ApiHelper
import com.example.movieapplication.api.RetrofitClient
import com.example.movieapplication.entity.People
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleViewModel : ViewModel() {

    companion object {
        private const val API_KEY = "b807fc0668b99af6a06ea5e34423da26"
        private const val urlImage = "https://image.tmdb.org/t/p/w92/"
    }

    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
    private val popularPeoples = MutableLiveData<ArrayList<People>>()


    fun setPopularPeople() {
        val peoples = ArrayList<People>()
        val queryMap = HashMap<String, String>()
        queryMap["api_key"] = API_KEY
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"

        val call = apiHelper.getPeoplePopular(queryMap)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure People", "onFailure : ${t.message}")
                popularPeoples.postValue(null)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val people = list.getJSONObject(i)
                        val peopleItem = People()
                        peopleItem.id = people.getInt("id")
                        peopleItem.departement = people.getString("known_for_department")
                        peopleItem.name = people.getString("name")
                        peopleItem.profilePath = "$urlImage${people.getString("profile_path")}"
                        peoples.add(peopleItem)
                    }
                    popularPeoples.postValue(peoples)

                } else {
                    popularPeoples.postValue(peoples)
                }
            }

        })
    }

    fun searchPeople(querySearch: String) {
        val peoples = ArrayList<People>()
        val query = HashMap<String, String>()
        query["api_key"] = API_KEY
        query["language"] = "en-US"
        query["query"] = querySearch
        query["page"] = "1"
        val call = apiHelper.getSearchPeople(query)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure People", "onFailure : ${t.message}")
                popularPeoples.postValue(null)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val responseObject = JSONObject(response.body()?.string())
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val people = list.getJSONObject(i)
                        val peopleItem = People()
                        peopleItem.id = people.getInt("id")
                        peopleItem.departement = people.getString("known_for_department")
                        peopleItem.name = people.getString("name")
                        peopleItem.profilePath = "$urlImage${people.getString("profile_path")}"
                        peoples.add(peopleItem)
                    }
                    popularPeoples.postValue(peoples)

                } else {
                    popularPeoples.postValue(peoples)
                }
            }

        })
    }

    fun getPopularPeople(): LiveData<ArrayList<People>> = popularPeoples


}