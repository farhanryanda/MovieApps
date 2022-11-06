package com.farhanryanda.challangechapter5.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    var id: Int,

    @field:ColumnInfo(name = "original_title")
    val originalTitle: String,

    @field:ColumnInfo(name = "poster_path")
    val posterPath: String,

    @field:ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    @field:ColumnInfo(name = "overview")
    val overview: String,
): Parcelable
