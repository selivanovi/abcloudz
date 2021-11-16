package com.example.networking.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.ui.recyclerviews.episodes.EpisodeLayoutManager
import com.example.networking.R
import com.example.networking.model.dao.Character
import com.example.networking.model.network.NetworkLayer
import com.example.networking.model.network.SharedRepository
import com.example.networking.ui.recyclerviews.episodes.EpisodeOffsetItemDecoration
import com.example.networking.ui.recyclerviews.episodes.EpisodesAdapter
import com.example.networking.setImageFromUrl
import com.example.networking.ui.viewmodels.DetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailsFragment : Fragment(R.layout.fragment_details) {


    private val detailsViewModel by viewModels<DetailsViewModel> {
        DetailsViewModel.Factory(SharedRepository(NetworkLayer.apiService))
    }

    private val character = runBlocking { detailsViewModel.channelCharacters.receive() }
    private val episode = runBlocking { detailsViewModel.channelEpisodes.receive() }
    private val error = runBlocking { detailsViewModel.channelError.receive() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notNullArguments = arguments ?: throw Exception()

        val id = notNullArguments.getInt(ARG_ID)

        detailsViewModel.getCharacterById(id)


        character.let {
            createView(view, it)
            createRecyclerView(view, character)
            Log.d("DetailsFragment", "Character: $character")
        }

        if (error != null)
        Toast.makeText(requireContext(), "Ups, something wrong!", Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun createView(view: View, character: Character) {
        val characterImageView = view.findViewById<ImageView>(R.id.characterImageView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val originTextView = view.findViewById<TextView>(R.id.originTextView)

        setImageFromUrl(character.image, characterImageView)
        nameTextView.text = character.name
        originTextView.text = character.origin?.name

    }

    private fun createRecyclerView(view: View, character: Character) {
        val adapter = EpisodesAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.episodesRecyclerView)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = EpisodeLayoutManager()

        recyclerView.addItemDecoration(EpisodeOffsetItemDecoration(25, 25, 25, 25))

        detailsViewModel.getEpisodesByIds(character)

        adapter.setData(episode)
    }

    companion object {
        const val ARG_ID = "argument_id"
    }
}