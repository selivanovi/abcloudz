package com.example.architecture.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.architecture.Constants
import com.example.architecture.R
import com.example.architecture.domain.entity.MovieDomain

class MovieAdapter(val onClick: (Long) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val movies = mutableListOf<MovieDomain>()

    fun setData(list: List<MovieDomain>) {
        movies.clear()
        movies.addAll(list)
        Log.d("MovieAdapter", list.toString())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        Glide.with(holder.imageView.context).load(Constants.IMAGE_URL + movies[position].posterPath)
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            onClick(movies[position].id)
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }


    class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById<ImageView>(R.id.posterImageView)
    }
}