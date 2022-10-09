package com.farhanryanda.challangechapter5

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.navigation.fragment.findNavController

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferences = this.getSharedPreferences(
            "datauser",
            Context.MODE_PRIVATE
        )

        supportActionBar?.hide()

        Handler().postDelayed({
            if (sharedPreferences.getString("username", "") == "" && sharedPreferences.getString("password", "") == "") {
                startActivity(Intent(this,LoginActivity::class.java))
            } else {
                startActivity(Intent(this,MainActivity::class.java))
            }

        }, 3000)
    }
}