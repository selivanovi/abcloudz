package com.example.architecture.screen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.architecture.Constants
import com.example.architecture.R
import com.example.architecture.app.appComponent
import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.viewmodel.DetailsMovieViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {



    @Inject
    lateinit var viewModel: DetailsMovieViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().appComponent.inject(this)
        val movieId = requireArguments().getLong(ARG_ID)

        viewModel.loadMovieById(movieId).onEach {
            createContent(view, it)
        }
    }

    private fun createContent(view: View, movieDomain: MovieDomain) {
        val imageView = view.findViewById<ImageView>(R.id.posterImageView)
        val title = view.findViewById<TextView>(R.id.titleTextView)

        Glide.with(view.context).load(Constants.IMAGE_URL+movieDomain.posterPath).into(imageView)
        title.text = movieDomain.title
    }

    companion object {
        const val ARG_ID = "movie_id"
    }
}