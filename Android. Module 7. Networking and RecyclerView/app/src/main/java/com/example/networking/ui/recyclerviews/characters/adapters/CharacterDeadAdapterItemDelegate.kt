package com.example.networking.ui.recyclerviews.characters.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.R
import com.example.networking.model.dao.AliveCharacter
import com.example.networking.model.dao.DeadCharacter
import com.example.networking.ui.delegate.BaseAdapterDelegate

class CharacterDeadAdapterItemDelegate<T : Any>(layoutId: Int): BaseAdapterDelegate<T>(layoutId) {

    override val map = mutableMapOf<Class<*>, Int>().also {
        it[DeadCharacter::class.java] = this.hashCode()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T) {
        (holder as CharacterViewHolder).bind(item as DeadCharacter)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        return CharacterViewHolder(LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false))
    }

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val statusTextView = view.findViewById<TextView>(R.id.statusTextView)
        private val speciesTextView = view.findViewById<TextView>(R.id.speciesTextView)

        fun bind(item: DeadCharacter?) {
            if (item != null) {
                nameTextView.text = item.name
                Log.d("CharacterDeadAdapter", "Name: " + item.name)
                statusTextView.text = item.status
                Log.d("CharacterDeadAdapter", "Status: " + item.status)
                speciesTextView.text = item.species
                Log.d("CharacterDeadAdapter", "Species: " + item.species)
            }
            else {
                nameTextView.text = null
                statusTextView.text = null
                speciesTextView.text = null
            }
        }
    }

}