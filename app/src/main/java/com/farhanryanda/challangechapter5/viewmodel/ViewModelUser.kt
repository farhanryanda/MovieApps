package com.farhanryanda.challangechapter5.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecolabs.model.ResponseUserItem
import com.farhanryanda.challangechapter5.model.DataUser
import com.farhanryanda.challangechapter5.model.DetailDataUser
import com.farhanryanda.challangechapter5.network.RetrofitUser

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelUser : ViewModel() {
    var getLdUser : MutableLiveData<List<ResponseUserItem>?> = MutableLiveData()
    var postLDUser: MutableLiveData<ResponseUserItem?> = MutableLiveData()
    var updLDUser: MutableLiveData<ResponseUserItem>
    var getUserById : MutableLiveData<ResponseUserItem?> = MutableLiveData()


    init {
        updLDUser = MutableLiveData()
    }

    fun postLiveDataUser(): MutableLiveData<ResponseUserItem?> {
        return postLDUser
    }

    fun updateLiveDataUser(): MutableLiveData<ResponseUserItem> {
        return updLDUser
    }

    fun getLiveDataUser(): MutableLiveData<List<ResponseUserItem>?> {
        return getLdUser
    }

    fun getLiveDataUserById() : MutableLiveData<ResponseUserItem?> {
        return getUserById
    }

    fun callGetUser() {
        RetrofitUser.instance.getAllUser()
            .enqueue(object  : Callback<List<ResponseUserItem>> {
                override fun onResponse(
                    call: Call<List<ResponseUserItem>>,
                    response: Response<List<ResponseUserItem>>
                ) {
                    if (response.isSuccessful) {
                        getLdUser.postValue(response.body())
                    } else {
                        getLdUser.postValue(null)
                    }
                }

                override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                    getLdUser.postValue(null)
                }

            })
    }

    fun callPostUser(name: String,
                     username: String,
                     password: String,
                     age: String,
                     address: String) {
        RetrofitUser.instance.postUser(DataUser(name, username, password,age, address))
            .enqueue(object : Callback<ResponseUserItem> {
                override fun onResponse(
                    call: Call<ResponseUserItem>,
                    response: Response<ResponseUserItem>
                ) {
                    if (response.isSuccessful) {
                        postLDUser.postValue(response.body())
                    } else {
                        postLDUser.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseUserItem>, t: Throwable) {
                    postLDUser.postValue(null)
                }

            })
    }

    fun updateApiUser(
        id: String,
        name: String,
        username: String,
        password: String,
        age: String,
        address: String
    ) {
        RetrofitUser.instance.updateUser(id, DataUser(name, username, password, age, address))
            .enqueue(object : Callback<ResponseUserItem> {
                override fun onResponse(
                    call: Call<ResponseUserItem>,
                    response: Response<ResponseUserItem>
                ) {
                    if (response.isSuccessful) {
                        updLDUser.postValue(response.body())
                    } else {
                        updLDUser.postValue(null!!)
                    }
                }

                override fun onFailure(call: Call<ResponseUserItem>, t: Throwable) {
                    updLDUser.postValue(null!!)
                }

            })
    }

    fun getUserById(id: String) {
        RetrofitUser.instance.getUserById(id)
            .enqueue(object : Callback<ResponseUserItem> {
                override fun onResponse(
                    call: Call<ResponseUserItem>,
                    response: Response<ResponseUserItem>
                ) {
                    if (response.isSuccessful) {
                        getUserById.postValue(response.body())
                    } else {
                        getUserById.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseUserItem>, t: Throwable) {
                    getUserById.postValue(null)
                }

            })
    }
}