package com.farhanryanda.challangechapter5.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.databinding.ActivityLoginBinding
import com.farhanryanda.challangechapter5.datastore.LoginDataStoreManager
import com.farhanryanda.challangechapter5.datastore.SavedPreference
import com.farhanryanda.challangechapter5.viewmodel.LoginViewModel
import com.farhanryanda.challangechapter5.viewmodel.ViewModelFactory
import com.farhanryanda.challangechapter5.viewmodel.ViewModelUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: LoginDataStoreManager
    private lateinit var viewModelLoginPref: LoginViewModel

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 1
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        FirebaseApp.initializeApp(this)

        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // initialize the firebaseAuth variable
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLoginGoogle.setOnClickListener { view: View? ->
            Toast.makeText(this,"Logging In",Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

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
            viewModelLoginPref =
                ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
            viewModel.callGetUser()
            viewModel.getLiveDataUser().observe(this, {
                if (it != null) {
                    for (i in it) {
                        if (i.username == username && i.password == password) {
                            viewModelLoginPref.setUserLogin(true)
                            viewModelLoginPref.saveUser(
                                i.id,
                                i.name,
                                i.username,
                                i.password,
                                i.age,
                                i.address
                            )
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

    private  fun signInGoogle(){

        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
//            firebaseAuthWithGoogle(account!!)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                SavedPreference.setEmail(this,account.email.toString())
                SavedPreference.setUsername(this,account.displayName.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}