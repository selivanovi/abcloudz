package com.example.networking.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.ui.recyclerviews.episodes.EpisodeLayoutManager
import com.example.networking.R
import com.example.networking.model.dao.Character
import com.example.networking.ui.recyclerviews.episodes.EpisodeOffsetItemDecoration
import com.example.networking.ui.recyclerviews.episodes.EpisodesAdapter
import com.example.networking.setImageFromUrl
import com.example.networking.ui.viewmodels.DetailsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val detailsViewModel by activityViewModels<DetailsViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val id = it.getInt(ARG_ID)
            lifecycleScope.launch {
                detailsViewModel.getCharacterById(id).collect { character ->
                    createView(view, character)
                    createRecyclerView(view, character)
                    Log.d("DetailsFragment", "Character: $character")
                }
            }
        }
    }

    private fun createView(view: View, character: Character) {
        val characterImageView = view.findViewById<ImageView>(R.id.characterImageView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val originTextView = view.findViewById<TextView>(R.id.originTextView)

        setImageFromUrl(character.image, characterImageView)
        nameTextView.text = character.name
        originTextView.text = character.origin.name

    }

    private fun createRecyclerView(view: View, character: Character) {
        val adapter = EpisodesAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.episodesRecyclerView)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = EpisodeLayoutManager()

        recyclerView.addItemDecoration(EpisodeOffsetItemDecoration(25, 25, 25, 25))

        lifecycleScope.launch {
            detailsViewModel.getEpisodesByIds(character).collect { episodes ->
                adapter.setData(episodes)
            }
        }
    }

    companion object {
        const val ARG_ID = "argument_id"
    }
}