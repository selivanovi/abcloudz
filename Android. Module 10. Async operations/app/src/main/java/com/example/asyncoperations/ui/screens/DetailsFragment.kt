package com.example.asyncoperations.ui.screens

import androidx.fragment.app.Fragment
import com.example.asyncoperations.R
import com.example.asyncoperations.ui.recyclerviews.episodes.EpisodesAdapter
import kotlinx.coroutines.*

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val adapter: EpisodesAdapter = EpisodesAdapter()

//    private val detailsViewModel by viewModels<DetailsViewModel> {
////        DetailsViewModel.Factory(EpisodesRepository(NetworkLayer.apiService))
//    }

    private val subscribeCharacter: Job by lazy {
        CoroutineScope(Dispatchers.Main).launch {
//            detailsViewModel.channelCharacters.consumeEach { character ->
//                view?.let { viewNotNull ->
//                    Log.d("DetailsFragment","Get character $character")
//                    setView(viewNotNull, character)
//                    setRecyclerView(viewNotNull)
//                    detailsViewModel.getEpisodesByIds(character)
//                }
        }
    }


    private val subscribeEpisode: Job by lazy {
        CoroutineScope(Dispatchers.Main).launch {
//            detailsViewModel.channelEpisodes.consumeEach { episodes ->
//                Log.d("DetailsFragment","Get episodes ${episodes.toString()}")
//                adapter.setData(episodes)
//            }
        }
    }

//    private val subscribeError: Job by lazy {
//        CoroutineScope(Dispatchers.Main).launch {
//            detailsViewModel.channelError.consumeEach { error ->
//                error?.let {
//                    Toast.makeText(requireContext(), "Ups, something wrong!", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//        super.onViewCreated(view, savedInstanceState)
//
//        val notNullArguments = arguments ?: throw Exception()
//
//        val id = notNullArguments.getInt(ARG_ID)
//
//        detailsViewModel.getCharacterById(id)
//
//        subscribeCharacter
//        subscribeEpisode
//        subscribeError
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        subscribeCharacter.cancel()
//        subscribeEpisode.cancel()
//        subscribeError.cancel()
//    }
//
//    private fun setView(view: View, character: CharacterUI) {
//        val characterImageView = view.findViewById<ImageView>(R.id.characterImageView)
//        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
//        val originTextView = view.findViewById<TextView>(R.id.originTextView)
//
//        Glide.with(view)
//            .load(character.image)
//            .centerCrop()
//            .placeholder(R.drawable.ic_baseline_image_24)
//            .into(characterImageView)
//        nameTextView.text = character.name
//        originTextView.text = character.species
//    }
//
//    private fun setRecyclerView(view: View) {
//        val recyclerView = view.findViewById<RecyclerView>(R.id.episodesRecyclerView)
//        val snapHelper = PagerSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerView)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = EpisodeLayoutManager()
//
//        recyclerView.addItemDecoration(EpisodeOffsetItemDecoration(25, 25, 25, 25))
//    }

//    companion object {
//        const val ARG_ID = "argument_id"
//    }
}