package com.farhanryanda.challangechapter5.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.databinding.FragmentDetailMovieBinding
import com.farhanryanda.challangechapter5.databinding.FragmentHomeBinding
import com.farhanryanda.challangechapter5.model.ResponsePopularMovie
import com.farhanryanda.challangechapter5.model.ResponsePopularMovieItem
import com.farhanryanda.challangechapter5.model.SerialResponseItem

class DetailMovieFragment : Fragment() {
    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

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

        if (arguments?.getSerializable("datadetail") != null) {
            var getDetailMovie = arguments?.getSerializable("datadetail") as ResponsePopularMovieItem
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + getDetailMovie.posterPath)
                .into(binding.ivMoviePoster)
            binding.titleText.text = getDetailMovie.originalTitle
            binding.tvOverview.text = getDetailMovie.overview
            binding.tvRate.text = getDetailMovie.voteAverage.toString()
        } else {
            var getDetailSerial = arguments?.getSerializable("dataserial") as SerialResponseItem

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+getDetailSerial.posterPath)
                .into(binding.ivMoviePoster)
            binding.titleText.text = getDetailSerial.originalName
            binding.tvOverview.text = getDetailSerial.overview
            binding.tvRate.text = getDetailSerial.voteAverage.toString()
        }

    }
}