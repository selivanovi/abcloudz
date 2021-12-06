package com.example.networking.ui.recyclerviews.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.R
import com.example.networking.model.dao.Episode

class EpisodesAdapter : RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {
    private var data = mutableListOf<Episode>()

    fun setData(list: List<Episode>) {
        data.clear()
        data.addAll(list)

        notifyDataSetChanged()
    }

    class EpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val dateTextView = view.findViewById<TextView>(R.id.airDateTextView)
        private val episodeTextView = view.findViewById<TextView>(R.id.episodeTextView)


        fun bind(episode: Episode) {
            nameTextView.text = episode.name
            dateTextView.text = episode.airData
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