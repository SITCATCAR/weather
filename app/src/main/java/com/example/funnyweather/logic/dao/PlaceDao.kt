package com.example.funnyweather.logic.dao

import android.content.Context
import android.provider.Settings.Global.putString
import androidx.core.content.edit
import com.example.funnyweather.FunnyWeatherApplication
import com.example.funnyweather.logic.model.Place
import com.google.gson.Gson

object PlaceDao {

    fun savePlace(place: Place){
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved()=sharedPreferences().contains("place")
    private fun sharedPreferences() = FunnyWeatherApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)


}