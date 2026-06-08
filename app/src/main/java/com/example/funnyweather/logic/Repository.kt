package com.example.funnyweather.logic

import androidx.lifecycle.liveData
import com.example.funnyweather.logic.dao.PlaceDao
import com.example.funnyweather.logic.model.DailyResponse
import com.example.funnyweather.logic.model.Place
import com.example.funnyweather.logic.model.Weather
import com.example.funnyweather.logic.network.FunnyWeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = FunnyWeatherNetWork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val realtimeResponse = FunnyWeatherNetWork.getRealtimeWeather(lng, lat)
            // QPS限制，1s内1次    429 错误
            kotlinx.coroutines.delay(1000)
            val dailyResponse = FunnyWeatherNetWork.getDailyWeather(lng, lat)
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime status is ${realtimeResponse.status}" +
                                "daily status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext,block:suspend ()-> Result<T>)=liveData<Result<T>>(context) {
        val result=try {
            block()
        }catch (e: Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

}