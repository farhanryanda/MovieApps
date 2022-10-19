package com.farhanryanda.challangechapter5.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.databinding.FragmentDetailMovieBinding
import com.farhanryanda.challangechapter5.databinding.FragmentHomeBinding
import com.farhanryanda.challangechapter5.model.ResponsePopularMovie
import com.farhanryanda.challangechapter5.model.ResponsePopularMovieItem
import com.farhanryanda.challangechapter5.model.SerialResponseItem
import com.farhanryanda.challangechapter5.viewmodel.FavoriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailMovieFragment : Fragment() {
    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this
        ).get(FavoriteViewModel::class.java)
        toggleFavorite()

        if (arguments?.getSerializable("datadetail") != null) {
            val getDetailMovie = arguments?.getSerializable("datadetail") as ResponsePopularMovieItem
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + getDetailMovie.posterPath)
                .into(binding.ivMoviePoster)
            binding.titleText.text = getDetailMovie.originalTitle
            binding.tvOverview.text = getDetailMovie.overview
            binding.tvRate.text = getDetailMovie.voteAverage.toString()
        } else {
            val getDetailSerial = arguments?.getSerializable("dataserial") as SerialResponseItem

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+getDetailSerial.posterPath)
                .into(binding.ivMoviePoster)
            binding.titleText.text = getDetailSerial.originalName
            binding.tvOverview.text = getDetailSerial.overview
            binding.tvRate.text = getDetailSerial.voteAverage.toString()
        }

    }

    fun toggleFavorite(){
        if (arguments?.getSerializable("datadetail") != null) {
            val getDetailMovie = arguments?.getSerializable("datadetail") as ResponsePopularMovieItem
            val id = getDetailMovie.id
            val originalName = getDetailMovie.originalTitle
            val voteAverage = getDetailMovie.voteAverage
            val overview = getDetailMovie.overview
            val posterPath = getDetailMovie.posterPath

            var _isChecked = false
            CoroutineScope(Dispatchers.IO).launch {
                val count = viewModel.checkUser(id)
                withContext(Dispatchers.Main){
                    if (count != null){
                        if (count > 0){
                            binding.btnFavorite.isChecked = true
                            _isChecked = true
                        } else {
                            binding.btnFavorite.isChecked = false
                            _isChecked = false
                        }
                    }
                }
            }


            binding.btnFavorite.setOnClickListener{
                _isChecked = !_isChecked
                if (_isChecked) {
                    viewModel.addToFavorite(id,originalName,posterPath, voteAverage, overview)
                } else {
                    viewModel.removeFromFavorite(id,originalName,posterPath, voteAverage, overview)
                }
                binding.btnFavorite.isChecked = _isChecked
            }
        } else {
            val getDetailSerial = arguments?.getSerializable("dataserial") as SerialResponseItem
            val id = getDetailSerial.id
            val originalName = getDetailSerial.originalName
            val voteAverage = getDetailSerial.voteAverage
            val overview = getDetailSerial.overview
            val posterPath = getDetailSerial.posterPath
            var _isChecked = false

            CoroutineScope(Dispatchers.IO).launch {
                val count = viewModel.checkUser(id)
                withContext(Dispatchers.Main){
                    if (count != null){
                        if (count > 0){
                            binding.btnFavorite.isChecked = true
                            _isChecked = true
                        } else {
                            binding.btnFavorite.isChecked = false
                            _isChecked = false
                        }
                    }
                }
            }
            
            binding.btnFavorite.setOnClickListener{
                _isChecked = !_isChecked
                if (_isChecked) {
                    viewModel.addToFavorite(id,originalName,posterPath, voteAverage, overview)
                } else {
                    viewModel.removeFromFavorite(id,originalName,posterPath, voteAverage, overview)
                }
                binding.btnFavorite.isChecked = _isChecked
            }
        }

    }
}