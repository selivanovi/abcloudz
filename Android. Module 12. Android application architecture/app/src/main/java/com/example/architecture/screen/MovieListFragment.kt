package com.example.architecture.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.architecture.recyclerview.MovieAdapter
import com.example.architecture.R
import com.example.architecture.app.appComponent
import com.example.architecture.retryIn
import com.example.architecture.viewmodel.MovieListViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val movieAdapter = MovieAdapter(::goToDetails)

    @Inject
    lateinit var viewModel: MovieListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().appComponent.inject(this)
        viewModel.channelMovie.onEach {
            Log.d("MovieListFragment", it.toString())
            movieAdapter.setData(it)
        }.retryIn(lifecycleScope)

        createRecyclerView(view)
    }

    private fun createRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManger = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManger
        recyclerView.adapter = movieAdapter
    }

    private fun goToDetails(movieId: Long) {
        val bundle = Bundle().apply {
            putLong(MovieDetailsFragment.ARG_ID, movieId)
        }
        findNavController().navigate(R.id.action_movieListFragment_to_movieDetailsFragment, bundle)
    }
}