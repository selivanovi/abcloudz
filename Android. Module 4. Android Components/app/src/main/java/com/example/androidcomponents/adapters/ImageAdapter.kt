package com.example.androidcomponents.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcomponents.R

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val images = mutableListOf<String>()

    fun setData(list: ArrayList<String>) {
        images.clear()
        images.addAll(list)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_image, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(string: String) {
            image.setImageURI(Uri.parse(string))
        }

    }
}