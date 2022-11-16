package com.farhanryanda.challangechapter5.network

import com.farhanryanda.challangechapter5.model.ResponseUserItem
import com.farhanryanda.challangechapter5.model.DataUser
import retrofit2.Call
import retrofit2.http.*

interface RestfulUser {

    @GET("users/")
    fun getAllUser(): Call<List<ResponseUserItem>>

    @POST("users")
    fun postUser(@Body request: DataUser): Call<ResponseUserItem>

    @PUT("users/{id}")
    fun updateUser(@Path("id") id : String, @Body request: DataUser ): Call<ResponseUserItem>

    @GET("users/{id}")
    fun getUserById(@Path("id") id : String) : Call<ResponseUserItem>
}