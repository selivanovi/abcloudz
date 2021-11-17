package com.example.networking.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networking.ui.recyclerviews.episodes.EpisodeLayoutManager
import com.example.networking.R
import com.example.networking.model.dao.Character
import com.example.networking.model.network.NetworkLayer
import com.example.networking.model.network.SharedRepository
import com.example.networking.ui.recyclerviews.episodes.EpisodeOffsetItemDecoration
import com.example.networking.ui.recyclerviews.episodes.EpisodesAdapter
import com.example.networking.ui.viewmodels.DetailsViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val adapter: EpisodesAdapter = EpisodesAdapter()

    private val detailsViewModel by viewModels<DetailsViewModel> {
        DetailsViewModel.Factory(SharedRepository(NetworkLayer.apiService))
    }

    private val subscribeCharacter: Job by lazy {
        CoroutineScope(Dispatchers.Main).launch {
            detailsViewModel.channelCharacters.consumeEach { character ->
                view?.let { viewNotNull ->
                    Log.d("DetailsFragment","Get character $character")
                    setView(viewNotNull, character)
                    setRecyclerView(viewNotNull)
                    detailsViewModel.getEpisodesByIds(character)
                }
            }
        }
    }

    private val subscribeEpisode: Job by lazy {
        CoroutineScope(Dispatchers.Main).launch {
            detailsViewModel.channelEpisodes.consumeEach { episodes ->
                Log.d("DetailsFragment","Get episodes ${episodes.toString()}")
                adapter.setData(episodes)
            }
        }
    }

    private val subscribeError: Job by lazy {
        CoroutineScope(Dispatchers.Main).launch {
            detailsViewModel.channelError.consumeEach { error ->
                error?.let {
                    Toast.makeText(requireContext(), "Ups, something wrong!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val notNullArguments = arguments ?: throw Exception()

        val id = notNullArguments.getInt(ARG_ID)

        detailsViewModel.getCharacterById(id)

        subscribeCharacter
        subscribeEpisode
        subscribeError
    }

    override fun onDestroy() {
        super.onDestroy()
        subscribeCharacter.cancel()
        subscribeEpisode.cancel()
        subscribeError.cancel()
    }

    private fun setView(view: View, character: Character) {
        val characterImageView = view.findViewById<ImageView>(R.id.characterImageView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val originTextView = view.findViewById<TextView>(R.id.originTextView)

        Glide.with(view)
            .load(character.image)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(characterImageView)
        nameTextView.text = character.name
        originTextView.text = character.origin?.name
    }

    private fun setRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.episodesRecyclerView)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = EpisodeLayoutManager()

        recyclerView.addItemDecoration(EpisodeOffsetItemDecoration(25, 25, 25, 25))
    }

    companion object {
        const val ARG_ID = "argument_id"
    }
}