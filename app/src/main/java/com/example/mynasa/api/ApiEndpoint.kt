package com.example.mynasa.api

import com.example.example.ResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
/*
* This class responsible for Api calls */
interface ApiEndpoint {

    @GET("planetary/apod")
    fun getPictureOftheDay(@Query("api_key") key: String,@Query("date") date: String): Call<ResultResponse>

    @GET("planetary/apod")
    fun getSomePictures(@Query("api_key") key: String,@Query("start_date") from: String,@Query("end_date") end: String):Call<List<ResultResponse>>
}