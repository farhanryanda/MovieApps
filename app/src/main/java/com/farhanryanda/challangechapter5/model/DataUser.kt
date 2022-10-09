package com.farhanryanda.challangechapter5.model

import java.io.Serializable

data class DataUser(
    val name: String,
    val username: String,
    val password: String,
    val age: String,
    val address: String
) : Serializable
