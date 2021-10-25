package com.example.networking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.model.Character

class CharacterAdapter(diffUtil: DiffUtil.ItemCallback<Character>) : PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(diffUtil) {

    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.findViewById<TextView>(R.id.textViewName)

        fun bind(item: Character?) {
            nameTextView.text = item?.name
        }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder =
        CharacterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false))
}