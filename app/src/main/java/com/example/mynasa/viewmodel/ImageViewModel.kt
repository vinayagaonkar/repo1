package com.example.mynasa.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.example.ResultResponse
import com.example.mynasa.activity.MainActivity.Companion.api_key
import com.example.mynasa.activity.MainActivity.Companion.baseUrl
import com.example.mynasa.activity.MainActivity.Companion.endDate
import com.example.mynasa.activity.MainActivity.Companion.fromDate
import com.example.mynasa.api.ApiEndpoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
*  This class communicates between UI and data*/
class ImageViewModel : ViewModel() {
    private lateinit var service: ApiEndpoint
    private  var listMutableResponse:MutableLiveData<List<ResultResponse>> = MutableLiveData<List<ResultResponse>>()
    private val list:ArrayList<ResultResponse> = ArrayList()

    fun init(){
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create<ApiEndpoint>(ApiEndpoint::class.java)
    }

    /*This method calls the api to get picture of the given date */
    fun getPictureOfTheDay(date:String){
        val call = service.getPictureOftheDay(api_key,date)
        call.enqueue(object : Callback<ResultResponse> {
            override fun onResponse(
                call: Call<ResultResponse>,
                response: Response<ResultResponse>
            ) {
                list.clear()
                response.body()?.let { list.add(it) }
                listMutableResponse.value=list
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                listMutableResponse.value = null
            }

        })
    }
    /*This method returns list of pictures between two dates*/
    fun getSomePictures(){
        val call = service.getSomePictures(api_key, fromDate, endDate)
        Log.d("validtag", "getSomePictures: call= "+call.request().url().toString())
        call.enqueue(object : Callback<List<ResultResponse>> {
            override fun onResponse(
                call: Call<List<ResultResponse>>,
                response: Response<List<ResultResponse>>
            ) {
                listMutableResponse.value = response?.body()
            }

            override fun onFailure(call: Call<List<ResultResponse>>, t: Throwable) {
                listMutableResponse.value = null
            }

        })
    }


    fun getPicturesList(): MutableLiveData<List<ResultResponse>> {
        return listMutableResponse
    }

 }

