package com.farhanryanda.challangechapter5

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.moviecolabs.model.ResponseUserItem
import com.farhanryanda.challangechapter5.databinding.ActivityLoginBinding
import com.farhanryanda.challangechapter5.network.RetrofitUser
import com.farhanryanda.challangechapter5.viewmodel.ViewModelUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreferences = this.getSharedPreferences("datauser",
            Context.MODE_PRIVATE)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding.btnIndo.setOnClickListener {
            setLocale("in")
        }

        binding.btnUsa.setOnClickListener {
            setLocale("en")
        }

        binding.btnLogin.setOnClickListener {

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val saveUser = sharedPreferences.edit()
            val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
            viewModel.callGetUser()
            viewModel.getLiveDataUser().observe(this, {
                if (it != null) {
                    for (i in it) {
                        if (i.username == username && i.password == password) {
                            saveUser.putString("id", i.id)
                            saveUser.putString("name", i.name)
                            saveUser.putString("username", i.username)
                            saveUser.putString("password", i.password)
                            saveUser.putString("address", i.address)
                            saveUser.putString("age", i.age)
                            saveUser.apply()
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

//            RetrofitUser.instance.getAllUser()
//                .enqueue(object : Callback<List<ResponseUserItem>> {
//                    override fun onResponse(
//                        call: Call<List<ResponseUserItem>>,
//                        response: Response<List<ResponseUserItem>>
//                    ) {
//                        if (response.isSuccessful) {
//                            val userList = response.body()?.filter {
//                                it.username == username && it.password == password
//                            } as List<ResponseUserItem>
//
//                            if (!userList.indices.isEmpty()) {
//                                val user = userList.first {
//                                    it.username == username && it.password == password
//                                }
//                                saveUser.putString("id", user.id)
//                                saveUser.putString("name", user.name)
//                                saveUser.putString("username", user.username)
//                                saveUser.putString("password", user.password)
//                                saveUser.putString("address", user.address)
//                                saveUser.putString("age", user.age.toString())
//                                saveUser.apply()
//
//                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                            } else {
//                                Toast.makeText(
//                                    this@LoginActivity,
//                                    "Gagal Login",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
//                    override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
//                        Toast.makeText(this@LoginActivity, "Gagal Login Fail", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                })
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