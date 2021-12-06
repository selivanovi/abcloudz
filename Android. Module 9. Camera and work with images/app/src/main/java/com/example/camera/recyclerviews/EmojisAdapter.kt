package com.example.camera.recyclerviews

import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.camera.CircleDrawable
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
        holder.bind(emojisList[position])

    }
    override fun getItemCount(): Int {
        return emojisList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById<ImageView>(R.id.itemEmoji)

        fun bind(item: Int) {
            imageView.setImageResource(item)
            imageView.setOnClickListener {

                onClickListener(item)
            }
        }
    }
}