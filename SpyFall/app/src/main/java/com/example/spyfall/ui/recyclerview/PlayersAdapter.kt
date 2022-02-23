package com.example.spyfall.ui.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.databinding.ItemRecyclerviewPlayerBinding
import com.example.spyfall.domain.entity.PlayerDomain

class PlayersAdapter : RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>() {

    private val players = mutableListOf<PlayerDomain>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newPlayers: List<PlayerDomain>) {
        players.clear()

        players.addAll(newPlayers)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemRecyclerviewPlayerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder.binding) {
            itemPlayer.text = players[position].name

            if (players[position].status == PlayerStatus.PLAY) {
                frameLayout.background =
                    AppCompatResources.getDrawable(
                        holder.itemView.context,
                        R.drawable.rounded_green_item_view
                    )
            } else {
                frameLayout.background =
                    AppCompatResources.getDrawable(
                        holder.itemView.context,
                        R.drawable.rounded_item_view
                    )
            }
        }
    }

    override fun getItemCount(): Int =
        players.size

    class PlayerViewHolder(val binding: ItemRecyclerviewPlayerBinding) :
        RecyclerView.ViewHolder(binding.root)
}
