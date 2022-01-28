package com.example.spyfall.ui.screen.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.PlayerDomain

class PlayersAdapter : RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>() {

    private val players = mutableListOf<PlayerDomain>()

    fun setData(newPlayers: List<PlayerDomain>) {
        players.clear()

        players.addAll(newPlayers)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_player, parent, false)

        return PlayerViewHolder(view)

    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.playerTextView.text = players[position].name
        if (players[position].status == PlayerStatus.PLAY) {
            holder.frameLayout.background =
                holder.itemView.resources.getDrawable(R.drawable.rounded_green_item_view, null)
        }
        else {
            holder.frameLayout.background =
                holder.itemView.resources.getDrawable(R.drawable.rounded_item_view, null)
        }
    }

    override fun getItemCount(): Int =
        players.size

    class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frameLayout = view.findViewById<FrameLayout>(R.id.frameLayout)
        val playerTextView = view.findViewById<TextView>(R.id.itemPlayer)
    }
}