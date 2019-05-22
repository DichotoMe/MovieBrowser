package com.dichotome.moviebrowser.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface APIService {
    @GET("/movies")
    fun fetchMovies() : Call<APIModel.Response>

    companion object {
        fun create(): APIService = Retrofit.Builder()
            .baseUrl("https://demo0216585.mockable.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}