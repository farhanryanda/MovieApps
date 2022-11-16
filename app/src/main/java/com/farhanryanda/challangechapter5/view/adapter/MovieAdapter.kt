package com.farhanryanda.challangechapter5.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.databinding.ItemMovieBinding
import com.farhanryanda.challangechapter5.model.ResponsePopularMovieItem

class MovieAdapter(private var listMovie: List<ResponsePopularMovieItem>): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.cvMovie.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("datadetail",listMovie[position])
            it.findNavController().navigate(R.id.action_homeFragment_to_detailMovieFragment, bundle)

        }
        holder.binding.tvTitle.text = listMovie[position].originalTitle
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500"+listMovie[position].posterPath)
            .into(holder.binding.imgPoster)
        holder.binding.tvVote.text = listMovie[position].voteAverage.toString()
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }
}