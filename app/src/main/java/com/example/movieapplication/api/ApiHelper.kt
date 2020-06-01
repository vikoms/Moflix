package com.example.movieapplication.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiHelper {

    @GET("movie/popular")
    fun getPopularMovie(@QueryMap queries: Map<String, String>): Call<ResponseBody>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@QueryMap queries: Map<String, String>): Call<ResponseBody>

    @GET("movie/upcoming")
    fun getUpcomingMovie(@QueryMap queries: Map<String, String>): Call<ResponseBody>

    @GET("tv/popular")
    fun getPopularSeries(@QueryMap queries: Map<String, String>): Call<ResponseBody>

    @GET("tv/airing_today")
    fun getAiringTodaySeries(@QueryMap queries: Map<String, String>): Call<ResponseBody>

    @GET("tv/on_the_air")
    fun getOnTvSeries(@QueryMap queries: Map<String, String>): Call<ResponseBody>

    @GET("person/popular")
    fun getPeoplePopular(@QueryMap queries: Map<String, String>): Call<ResponseBody>

    @GET("search/person")
    fun getSearchPeople(@QueryMap queries: Map<String, String>): Call<ResponseBody>

    @GET("movie/{id}")
    fun getDetailMovie(
        @Path("id") id: String,
        @QueryMap queries: Map<String, String>
    ): Call<ResponseBody>

    @GET("tv/{id}")
    fun getDetailSeries(
        @Path("id") id: String,
        @QueryMap queries: Map<String, String>
    ): Call<ResponseBody>

    @GET("{type}/{id}/credits")
    fun getCast(
        @Path("type") type: String,
        @Path("id") id: String,
        @QueryMap queries: Map<String, String>
    ): Call<ResponseBody>

    @GET("search/{type}")
    fun getSearchMovieAndSeries(
        @Path("type") type: String,
        @QueryMap queries: Map<String, String>
    ): Call<ResponseBody>

    @GET("person/{id}")
    fun getDetailPeople(
        @Path("id") id: String,
        @QueryMap queries: Map<String, String>
    ): Call<ResponseBody>

    @GET("person/{id}/movie_credits")
    fun getMovieCreditsPeople(
        @Path("id") id: String,
        @QueryMap queries: Map<String, String>
    ): Call<ResponseBody>

    @GET("person/{id}/tv_credits")
    fun getSeriesCreditsPeople(
        @Path("id") id: String,
        @QueryMap queries: Map<String, String>
    ): Call<ResponseBody>


    @GET("discover/movie")
    fun getMovieReleaseNow(@QueryMap queries: Map<String, String>): Call<ResponseBody>


}