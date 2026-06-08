package com.example.funnyweather.logic.network

import com.example.funnyweather.FunnyWeatherApplication
import com.example.funnyweather.logic.model.DailyResponse
import com.example.funnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.6/${FunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    @GET("v2.6/${FunnyWeatherApplication.TOKEN}/{lng},{lat}/daily")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>


}