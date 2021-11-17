package com.example.networking.ui.recyclerviews.characters.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networking.R
import com.example.networking.model.dao.AliveCharacter
import com.example.networking.ui.delegate.BaseAdapterDelegate

class CharacterAliveAdapterItemDelegate<T : Any>(layoutId: Int) : BaseAdapterDelegate<T>(layoutId) {

    override val map = mutableMapOf<Class<*>, Int>().also {
        it[AliveCharacter::class.java] = this.hashCode()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T) {
        (holder as CharacterViewHolder).bind(item as AliveCharacter)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        return CharacterViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
        )
    }

    class CharacterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val imageCharacter = view.findViewById<ImageView>(R.id.characterImageView)
        private val statusTextView = view.findViewById<TextView>(R.id.statusTextView)
        private val speciesTextView = view.findViewById<TextView>(R.id.speciesTextView)


        fun bind(item: AliveCharacter?) {
            if (item != null) {
                nameTextView.text = item.name
                Log.d("CharacterDeadAdapter", "Name: " + item.name)
                Glide.with(view)
                    .load(item.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(imageCharacter)
                Log.d("CharacterAliveAdapter", "Image: " + item.image)
                statusTextView.text = item.status
                Log.d("CharacterDeadAdapter", "Status: " + item.status)
                speciesTextView.text = item.species
                Log.d("CharacterDeadAdapter", "Species: " + item.species)
            }
            else {
                nameTextView.text = null
                imageCharacter.setImageBitmap(null)
                statusTextView.text = null
                speciesTextView.text = null
            }
        }
    }
}