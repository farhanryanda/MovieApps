package com.farhanryanda.challangechapter5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.farhanryanda.challangechapter5.model.FavoriteEntity
import com.farhanryanda.challangechapter5.room.FavoriteDatabase
import com.farhanryanda.challangechapter5.room.FavoriteMovieDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var favoriteDao: FavoriteMovieDao?
    private var favoriteDatabase: FavoriteDatabase?

    init {
        favoriteDatabase = FavoriteDatabase.getDatabase(application)
        favoriteDao = favoriteDatabase?.favoriteMovieDao()
    }

    fun addToFavorite(id: Int,
                      originalTitle: String,
                      posterPath: String,
                      voteAverage: Double,
                      overview: String ) {
        CoroutineScope(Dispatchers.IO).launch {
            var movie = FavoriteEntity(id, originalTitle, posterPath, voteAverage, overview)
            favoriteDao?.insertFavorite(movie)
        }
    }

    suspend fun checkUser(id: Int) = favoriteDao?.checkMovie(id)

    fun removeFromFavorite(id: Int,
                           originalTitle: String,
                           posterPath: String,
                           voteAverage: Double,
                           overview: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var movie = FavoriteEntity(id, originalTitle, posterPath, voteAverage, overview)
            favoriteDao?.deleteFavorite(movie)
        }
    }

    fun getFavoriteMovie(): LiveData<List<FavoriteEntity>>? {
        return favoriteDao?.getDataFavorite()
    }
}