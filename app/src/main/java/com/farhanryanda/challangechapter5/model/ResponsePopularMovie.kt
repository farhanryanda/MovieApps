package com.farhanryanda.challangechapter5.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class ResponsePopularMovie(
    @SerializedName("results")
    val results: List<ResponsePopularMovieItem>
)