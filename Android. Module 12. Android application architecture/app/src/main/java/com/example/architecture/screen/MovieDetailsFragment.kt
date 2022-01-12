package com.example.architecture.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.architecture.Constants
import com.example.architecture.R
import com.example.architecture.app.appComponent
import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.viewmodel.DetailsMovieViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    @Inject
    lateinit var viewModel: DetailsMovieViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().appComponent.inject(this)
        val movieId = requireArguments().getLong(ARG_ID)

        viewModel.observeMovieById(movieId).onEach {
            Log.d("MovieDetailsFragment", it.title.toString())
            createContent(view, it)
        }.launchIn(lifecycleScope)
    }

    private fun createContent(view: View, movieDomain: MovieDomain) {
        val imageView = view.findViewById<ImageView>(R.id.posterImageView)
        val title = view.findViewById<AppCompatTextView>(R.id.titleTextView)

        Glide.with(view.context).load(Constants.IMAGE_URL + movieDomain.posterPath).into(imageView)
        title.text = movieDomain.title
    }

    companion object {
        private const val ARG_ID = "movie_id"

        fun newInstance(movieId: Long): Bundle =
            Bundle().apply {
                putLong(MovieDetailsFragment.ARG_ID, movieId)
            }

    }
}