package com.farhanryanda.challangechapter5.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farhanryanda.challangechapter5.R
import com.farhanryanda.challangechapter5.databinding.ItemMovieBinding
import com.farhanryanda.challangechapter5.model.SerialResponseItem


class SerialAdapter (var listSerial: List<SerialResponseItem>): RecyclerView.Adapter<SerialAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSerial.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.cvMovie.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable("dataserial",listSerial[position])
            it.findNavController().navigate(R.id.action_homeFragment_to_detailMovieFragment, bundle)
        }
        holder.binding.tvTitle.text = listSerial[position].originalName
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500"+listSerial[position].posterPath)
            .into(holder.binding.imgPoster)
        holder.binding.tvVote.text = listSerial[position].voteAverage.toString()
    }
}