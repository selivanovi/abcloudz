package com.example.asyncoperations.ui.recyclerviews.characters.adapters

import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter(val onClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    var characterList = mutableListOf<CharacterUI>()
        set(value) {
            field.clear()
            field = value
        }

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}