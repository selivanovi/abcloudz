package com.example.spyfall.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.databinding.ItemCheckableRecyclerviewBinding
import com.example.spyfall.domain.entity.PlayerDomain

class VotesAdapter(
    private val changeItemListener: (playerDomain: PlayerDomain) -> Unit
) : RecyclerView.Adapter<VotesAdapter.VoteViewHolder>() {


    private var selectedPosition: Int = -1

    private val players = mutableListOf<PlayerDomain>()

    fun setData(newPlayers: List<PlayerDomain>) {
        players.clear()

        players.addAll(newPlayers)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val binding = ItemCheckableRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return VoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {

        with(holder.binding) {
            itemPlayer.text = players[position].name

            holder.binding.voteCheckBox.isChecked = holder.adapterPosition == selectedPosition

            voteCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {

                    frameLayout.background =
                        AppCompatResources.getDrawable(
                            holder.itemView.context,
                            R.drawable.rounded_fill_item_view
                        )

                    selectedPosition = holder.adapterPosition
                    changeItemListener(players[position])
                    notifyDataSetChanged()
                } else {
                    frameLayout.background =
                        AppCompatResources.getDrawable(
                            holder.itemView.context,
                            R.drawable.rounded_item_view
                        )
                }
            }
        }
    }

    override fun getItemCount(): Int =
        players.size

    class VoteViewHolder(val binding: ItemCheckableRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}
