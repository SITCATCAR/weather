package com.example.funnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object FunnyWeatherNetWork {
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create<WeatherService>()
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()
    suspend fun getDailyWeather(lng: String, lat: String)=weatherService.getDailyWeather(lng,lat).await()
    suspend fun getRealtimeWeather(lng: String, lat: String)=weatherService.getRealtimeWeather(lng,lat).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) continuation.resume(body)
                        else continuation.resumeWithException(
                            RuntimeException("response body is null from ${call.request().url()}"))
                    } else {
                        continuation.resumeWithException(
                            RuntimeException("response status code is ${response.code()}: ${response.errorBody()?.string()} from ${call.request().url()}"))
                    }
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}