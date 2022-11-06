package com.farhanryanda.challangechapter5.network

import com.farhanryanda.challangechapter5.model.ResponsePopularMovie
import com.farhanryanda.challangechapter5.model.SerialResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/3/movie/popular?api_key=936f5f6c2a6d04c4cbf7d4b54e9e5657")
    fun getPopularMovie(): Call<ResponsePopularMovie>

    @GET("/3/tv/popular?api_key=936f5f6c2a6d04c4cbf7d4b54e9e5657")
    fun getTvSerial(): Call<SerialResponse>
}