package com.farhanryanda.challangechapter5.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.view.adapter.MovieAdapter
import com.farhanryanda.challangechapter5.view.adapter.SerialAdapter
import com.farhanryanda.challangechapter5.databinding.FragmentHomeBinding
import com.farhanryanda.challangechapter5.datastore.LoginDataStoreManager
import com.farhanryanda.challangechapter5.model.ResponsePopularMovieItem
import com.farhanryanda.challangechapter5.model.SerialResponseItem
import com.farhanryanda.challangechapter5.viewmodel.LoginViewModel
import com.farhanryanda.challangechapter5.viewmodel.ViewModelFactory
import com.farhanryanda.challangechapter5.viewmodel.ViewModelPopularMovie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first


class HomeFragment : Fragment() {
    private lateinit var pref: LoginDataStoreManager
    private lateinit var viewModelLoginPref: LoginViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = LoginDataStoreManager(this.requireActivity())
        viewModelLoginPref = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
        viewModelLoginPref.getUser().observe(this.requireActivity(),{
            binding.tvSayHello.text = "Welcome, " + it.name
        })
        showDataMoviePopoular()
        showDataSerialPopular()

        binding.btnFavorite.setOnClickListener {
            this.findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }
    }


    fun showDataMoviePopoular() {
        val viewModel = ViewModelProvider(this).get(ViewModelPopularMovie::class.java)
        viewModel.callApiPopularMovie{movies: List<ResponsePopularMovieItem> ->
            binding.rvMovie1.adapter = MovieAdapter(movies)
        }
        binding.rvMovie1.layoutManager = LinearLayoutManager(this.requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvMovie1.setHasFixedSize(true)
    }

    fun showDataSerialPopular() {
        val viewModel = ViewModelProvider(this).get(ViewModelPopularMovie::class.java)
        viewModel.callApiTvSerial{serial: List<SerialResponseItem> ->
            binding.rvMovie2.adapter = SerialAdapter(serial)
        }
        binding.rvMovie2.layoutManager = LinearLayoutManager(this.requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvMovie2.setHasFixedSize(true)
    }
}