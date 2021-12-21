package com.example.android.todolistapplication.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.weatherapi.com/v1/"
private const val API_KEY = "b3795a8af51c4c8ea1e144005212112"
private const val CITY_NAME = "Debrecen"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {

    @GET("current.json?key=${API_KEY}&q=${CITY_NAME}")
    suspend fun getWeatherInfo(): WeatherInfo

}

object WeatherApi {

    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

}
