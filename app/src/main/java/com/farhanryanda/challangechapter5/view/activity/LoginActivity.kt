package com.farhanryanda.challangechapter5.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.farhanryanda.challangechapter5.databinding.ActivityLoginBinding
import com.farhanryanda.challangechapter5.datastore.LoginDataStoreManager
import com.farhanryanda.challangechapter5.viewmodel.LoginViewModel
import com.farhanryanda.challangechapter5.viewmodel.ViewModelFactory
import com.farhanryanda.challangechapter5.viewmodel.ViewModelUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: LoginDataStoreManager
    private lateinit var viewModelLoginPref: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnIndo.setOnClickListener {
            setLocale("in")
        }

        binding.btnUsa.setOnClickListener {
            setLocale("en")
        }

        binding.btnLogin.setOnClickListener {
            pref = LoginDataStoreManager(this)
            viewModelLoginPref = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
            viewModel.callGetUser()
            viewModel.getLiveDataUser().observe(this, {
                if (it != null) {
                    for (i in it) {
                        if (i.username == username && i.password == password) {
                            viewModelLoginPref.setUserLogin(true)
                            viewModelLoginPref.saveUser(i.id,i.name,i.username,i.password,i.age,i.address)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Gagal Login",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }
    }

    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        startActivity(Intent(this, LoginActivity::class.java))
    }
}