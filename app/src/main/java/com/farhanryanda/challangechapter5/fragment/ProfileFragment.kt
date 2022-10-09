package com.farhanryanda.challangechapter5.fragment


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moviecolabs.model.ResponseUserItem
import com.farhanryanda.challangechapter5.LoginActivity
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.databinding.FragmentProfileBinding
import com.farhanryanda.challangechapter5.model.DataUser
import com.farhanryanda.challangechapter5.model.DetailDataUser
import com.farhanryanda.challangechapter5.viewmodel.ViewModelUser


class ProfileFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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
        sharedPreferences = requireActivity().applicationContext.getSharedPreferences("datauser",
            Context.MODE_PRIVATE)
        
//        binding..text = "Welcome, " + sharedPreferences.getString("username","")
        binding.btnLogout.setOnClickListener {
            alertDialog()
        }
        getUserById()

        binding.btnUpdate.setOnClickListener {
            updateUser()
            this.findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
    }

    private fun getUserById() {
        var id = sharedPreferences.getString("id","").toString()
        val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
        viewModel.getUserById(id)
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
        var id = sharedPreferences.getString("id","").toString()
        var name = sharedPreferences.getString("name","").toString()
        var username = binding.etUsername.text.toString()
        var password = binding.etPassword.text.toString()
        var address = binding.etAddress.text.toString()
        var age = binding.etAge.text.toString()
        val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
        viewModel.updateApiUser(id, name, username, password, age, address)
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
            val saveUser = sharedPreferences.edit()
            saveUser.clear()
            saveUser.apply()
            Toast.makeText(requireActivity(),"Anda Logout", Toast.LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}