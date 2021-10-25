package com.example.networking

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.model.Character

class CharacterAdapter(diffUtil: DiffUtil.ItemCallback<Character>) : PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(diffUtil) {

    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val imageCharacter = view.findViewById<ImageView>(R.id.characterImageView)
        private val statusTextView = view.findViewById<TextView>(R.id.statusTextView)
        private val speciesTextView = view.findViewById<TextView>(R.id.speciesTextView)

        fun bind(item: Character?) {
            item?.let { character->
                nameTextView.text = character.name
                Log.d("CharacterAdapter", "Name: " + character.name)
                setImageFromUrl(character.image, imageCharacter)
                Log.d("CharacterAdapter", "Image: " + character.image)
                statusTextView.text = character.status
                Log.d("CharacterAdapter", "Status: " + character.status)
                speciesTextView.text = character.species
                Log.d("CharacterAdapter", "Species: " + character.species)
            }
        }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder =
        CharacterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false))
}