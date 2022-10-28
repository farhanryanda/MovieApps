package com.farhanryanda.challangechapter5.view.fragment


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.farhanryanda.blurimagewm.workers.KEY_IMAGE_URI
import com.farhanryanda.challangechapter5.view.activity.LoginActivity
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.databinding.FragmentProfileBinding
import com.farhanryanda.challangechapter5.datastore.LoginDataStoreManager
import com.farhanryanda.challangechapter5.viewmodel.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val REQUEST_CODE_PERMISSION = 100
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var pref: LoginDataStoreManager
    private lateinit var viewModelLoginPref: LoginViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BlurViewModel by viewModels { BlurViewModelFactory(this.requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this.requireActivity(),gso)

        sharedPreferences = requireActivity().applicationContext.getSharedPreferences("datauser",
            Context.MODE_PRIVATE)
        pref = LoginDataStoreManager(this.requireActivity())
        binding.btnLogout.setOnClickListener {
            mGoogleSignInClient.signOut()
            alertDialog()
        }
        getUserById()

        binding.btnUpdate.setOnClickListener {
            updateUser()
            viewModel.applyBlur(3)
            this.findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }

        binding.imgProfile.setOnClickListener {
            checkingPermissions()
        }
    }

    private fun getUserById() {
        val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
        viewModelLoginPref = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
        viewModelLoginPref.getUser().observe(this.requireActivity(), {
            viewModel.getUserById(it.id)
        })
        viewModel.getLiveDataUserById().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.etUsername.setText(it.username)
                binding.etPassword.setText(it.password)
                binding.etAddress.setText(it.address)
                binding.etAge.setText(it.age)
            }
        }
    }

    private fun updateUser() {
        var username = binding.etUsername.text.toString()
        var password = binding.etPassword.text.toString()
        var address = binding.etAddress.text.toString()
        var age = binding.etAge.text.toString()
        val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
        viewModelLoginPref.getUser().observe(this.requireActivity(), {
            viewModel.updateApiUser(it.id, it.name, username, password, age, address)
        })
        viewModel.updateLiveDataUser().observe(this.requireActivity(), {
            if (it != null){
                Toast.makeText(this.requireActivity(), "Update Data Success", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun alertDialog(){
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle("Logout")

        builder.setMessage("Apakah Anda yakin untuk Keluar ?")

        builder.setNegativeButton("Tidak"){dialogInterface, which ->
            Toast.makeText(requireActivity(),"Tidak", Toast.LENGTH_LONG).show()
        }

        builder.setPositiveButton("Ya"){dialogInterface, which->
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            viewModelLoginPref = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
            viewModelLoginPref.saveUser("","","","","","")
            viewModelLoginPref.setUserLogin(false)
            Toast.makeText(requireActivity(),"Anda Logout", Toast.LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }



    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            viewModel.getImageUri(result!!)
            binding.imgProfile.setImageURI(result)
        }

    private fun checkingPermissions() {
        if (isGranted(
                this.requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this.requireActivity())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this.requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun chooseImageDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this.requireActivity())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private fun openGallery() {
        this.requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap

        binding.imgProfile.setImageBitmap(bitmap)

    }
}