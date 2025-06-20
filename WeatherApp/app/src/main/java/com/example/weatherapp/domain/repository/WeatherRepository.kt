package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.util.Resource
import com.example.weatherapp.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(long: Double, lat: Double): Resource<WeatherInfo>
}