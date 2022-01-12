package com.example.camera.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.camera.customviews.CircleDrawable
import com.example.camera.R

class EmojisAdapter(val onClickListener: (Int) -> Unit) : RecyclerView.Adapter<EmojisAdapter.ViewHolder>() {

    private var currentPosition: CircleDrawable? = null

    private val emojisList = mutableListOf<Int>()

    fun setData(list: List<Int>) {
        with(emojisList) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_emoji, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(emojisList[position])
        holder.imageView.setOnClickListener {
            onClickListener(emojisList[position])
        }
    }
    override fun getItemCount(): Int {
        return emojisList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itemEmoji)
    }
}