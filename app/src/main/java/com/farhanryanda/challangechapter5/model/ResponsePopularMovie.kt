package com.farhanryanda.challangechapter5.model


import com.google.gson.annotations.SerializedName


data class ResponsePopularMovie(
    @SerializedName("results")
    val results: List<ResponsePopularMovieItem>
)