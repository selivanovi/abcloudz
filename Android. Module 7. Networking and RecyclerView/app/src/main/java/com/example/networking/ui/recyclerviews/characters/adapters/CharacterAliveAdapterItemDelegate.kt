package com.example.networking.ui.recyclerviews.characters.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.R
import com.example.networking.model.dao.AliveCharacter
import com.example.networking.setImageFromUrl
import com.example.networking.ui.delegate.BaseAdapterDelegate

class CharacterAliveAdapterItemDelegate<T>(layoutId: Int): BaseAdapterDelegate<T>(layoutId) {

    override fun isForViewType(item: T): Boolean {
        return item is AliveCharacter
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T) {
        (holder as CharacterViewHolder).bind(item as AliveCharacter)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        return CharacterViewHolder(LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false))
    }

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val imageCharacter = view.findViewById<ImageView>(R.id.characterImageView)
        private val statusTextView = view.findViewById<TextView>(R.id.statusTextView)
        private val speciesTextView = view.findViewById<TextView>(R.id.speciesTextView)

        fun bind(item: AliveCharacter?) {
            item?.let { character->
                nameTextView.text = character.name
                Log.d("CharacterAliveAdapter", "Name: " + character.name)
                setImageFromUrl(character.image, imageCharacter)
                Log.d("CharacterAliveAdapter", "Image: " + character.image)
                statusTextView.text = character.status
                Log.d("CharacterAliveAdapter", "Status: " + character.status)
                speciesTextView.text = character.species
                Log.d("CharacterAliveAdapter", "Species: " + character.species)
            }
        }
    }

}