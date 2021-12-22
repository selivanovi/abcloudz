package com.example.architecture.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.architecture.R
import com.example.architecture.viewmodel.MovieListViewModel

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val viewModel by viewModels<MovieListViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        const val ARG_ID = "movie_id"
    }
}