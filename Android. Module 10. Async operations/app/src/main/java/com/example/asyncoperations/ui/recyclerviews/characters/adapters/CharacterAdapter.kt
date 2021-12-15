package com.example.asyncoperations.ui.recyclerviews.characters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asyncoperations.R
import com.example.asyncoperations.model.ui.CharacterUI
import com.example.asyncoperations.utils.CharacterComparator
import com.example.asyncoperations.utils.StatusOfCharacters

class CharacterAdapter() :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private val characterList = mutableListOf<CharacterUI>()

    fun setData(list: List<CharacterUI>) {
        val diffUtil = CharacterComparator(characterList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        with(characterList) {
            clear()
            addAll(list)
        }
        diffResult.dispatchUpdatesTo(this)
    }


    class CharacterViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.characterImageView)
        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val indicatorLayout = view.findViewById<FrameLayout>(R.id.indicatorLayout)
        private val statusTextView = view.findViewById<TextView>(R.id.statusTextView)
        private val speciesTextView = view.findViewById<TextView>(R.id.speciesTextView)


        fun bind(item: CharacterUI) {
            Glide.with(view)
                .load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(imageView)

            nameTextView.text = item.name

            when (item.status?.uppercase()) {
                StatusOfCharacters.ALIVE.status.uppercase() -> indicatorLayout.setBackgroundResource(
                    R.drawable.grean_circle
                )
                StatusOfCharacters.DEAD.status.uppercase() -> indicatorLayout.setBackgroundResource(
                    R.drawable.red_circle
                )
                else -> indicatorLayout.setBackgroundResource(R.drawable.gray_circle)
            }

            statusTextView.text = item.status
            speciesTextView.text = item.species
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recyclerview, parent, false)
        )

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    override fun getItemCount(): Int = characterList.size
}