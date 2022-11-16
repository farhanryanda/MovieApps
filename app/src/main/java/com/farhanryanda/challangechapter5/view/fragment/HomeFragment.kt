@file:Suppress("MoveLambdaOutsideParentheses", "MoveLambdaOutsideParentheses",
    "MoveLambdaOutsideParentheses", "MoveLambdaOutsideParentheses", "MoveLambdaOutsideParentheses",
    "MoveLambdaOutsideParentheses"
)

package com.farhanryanda.challangechapter5.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.databinding.FragmentHomeBinding
import com.farhanryanda.challangechapter5.model.ResponsePopularMovieItem
import com.farhanryanda.challangechapter5.model.SerialResponseItem
import com.farhanryanda.challangechapter5.view.adapter.MovieAdapter
import com.farhanryanda.challangechapter5.view.adapter.SerialAdapter
import com.farhanryanda.challangechapter5.viewmodel.LoginViewModel
import com.farhanryanda.challangechapter5.viewmodel.ViewModelFactory
import com.farhanryanda.challangechapter5.viewmodel.ViewModelPopularMovie


class HomeFragment : Fragment() {
    private lateinit var pref: com.farhanryanda.challangechapter5.datastore.LoginDataStoreManager
    private lateinit var viewModelLoginPref: LoginViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = com.farhanryanda.challangechapter5.datastore.LoginDataStoreManager(this.requireActivity())
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


    private fun showDataMoviePopoular() {
        val viewModel = ViewModelProvider(this)[ViewModelPopularMovie::class.java]
        viewModel.callApiPopularMovie{movies: List<ResponsePopularMovieItem> ->
            binding.rvMovie1.adapter = MovieAdapter(movies)
        }
        binding.rvMovie1.layoutManager = LinearLayoutManager(this.requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvMovie1.setHasFixedSize(true)
    }

    private fun showDataSerialPopular() {
        val viewModel = ViewModelProvider(this)[ViewModelPopularMovie::class.java]
        viewModel.callApiTvSerial{serial: List<SerialResponseItem> ->
            binding.rvMovie2.adapter = SerialAdapter(serial)
        }
        binding.rvMovie2.layoutManager = LinearLayoutManager(this.requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvMovie2.setHasFixedSize(true)
    }
}