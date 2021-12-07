package com.example.camera.recyclerviews

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.camera.CircleDrawable
import com.example.camera.Filter
import com.example.camera.R

class FiltersAdapter(val onClickListener: (Filter) -> Unit, var bitmap: Bitmap?) : RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

    private val filtersList = mutableListOf<Filter>()

    fun setData(list: List<Filter>) {
        with(filtersList) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.filter_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filtersList[position])

    }

    override fun getItemCount(): Int {
        return filtersList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById<ImageView>(R.id.filterItem)

        fun bind(item: Filter) {
            imageView.setImageBitmap(bitmap)
            imageView.setColorFilter(item.color, item.mode)
            imageView.setOnClickListener {
                onClickListener(item)
            }
        }
    }
}