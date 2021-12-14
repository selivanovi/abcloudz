package com.example.asyncoperations.ui.recyclerviews.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asyncoperations.R
import com.example.asyncoperations.model.room.entities.Episode
import com.example.asyncoperations.model.ui.CharacterUI
import com.example.asyncoperations.model.ui.EpisodeUI
import com.example.asyncoperations.utils.CharacterComparator
import com.example.asyncoperations.utils.EpisodesComparator

class EpisodesAdapter : RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {
    private var data = mutableListOf<EpisodeUI>()

    fun setData(list: List<EpisodeUI>) {
        val diffUtil = EpisodesComparator(data, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        with(data) {
            clear()
            addAll(list)
        }
        diffResult.dispatchUpdatesTo(this)
    }


    class EpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val dateTextView = view.findViewById<TextView>(R.id.airDateTextView)
        private val episodeTextView = view.findViewById<TextView>(R.id.episodeTextView)


        fun bind(episode: EpisodeUI) {
            nameTextView.text = episode.name
            dateTextView.text = episode.airDate
            episodeTextView.text = episode.episode
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder =
        EpisodeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.episode_item_recyclerview, parent, false)
        )

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}