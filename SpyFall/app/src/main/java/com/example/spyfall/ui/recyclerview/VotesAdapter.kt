package com.example.spyfall.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.domain.entity.PlayerDomain

class VotesAdapter(
    private val changeItemListener: (playerDomain: PlayerDomain) -> Unit
) : RecyclerView.Adapter<VotesAdapter.VoteViewHolder>() {

    private var currentCheckBox: CheckBox? = null

    private val players = mutableListOf<PlayerDomain>()

    fun setData(newPlayers: List<PlayerDomain>) {
        players.clear()

        players.addAll(newPlayers)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkable_recyclerview, parent, false)

        return VoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        holder.playerTextView.text = players[position].name
        holder.voteCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                currentCheckBox?.let {
                    if (it.isChecked) it.isChecked = false
                }

                changeItemListener(players[position])

                currentCheckBox = holder.voteCheckBox
                holder.frameLayout.background =
                    holder.view.resources.getDrawable(R.drawable.rounded_fill_item_view, null)
            } else {
                holder.frameLayout.background =
                    holder.view.resources.getDrawable(R.drawable.rounded_item_view, null)
            }
        }
    }

    override fun getItemCount(): Int =
        players.size

    class VoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val frameLayout = view.findViewById<FrameLayout>(R.id.frameLayout)
        val playerTextView = view.findViewById<TextView>(R.id.itemPlayer)
        val voteCheckBox = view.findViewById<CheckBox>(R.id.voteCheckBox)

    }
}