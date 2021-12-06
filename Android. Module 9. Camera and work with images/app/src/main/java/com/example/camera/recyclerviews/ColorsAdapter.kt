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

class ColorsAdapter(val onClickListener: (Int) -> Unit) : RecyclerView.Adapter<ColorsAdapter.ViewHolder>() {

    private var currentPosition: CircleDrawable? = null

    private val colorList = mutableListOf<Int>()

    fun setData(list: List<Int>) {
        with(colorList) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(colorList[position])

    }
    override fun getItemCount(): Int {
        return colorList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val drawableItem = CircleDrawable()
        val imageView: ImageView = view.findViewById<ImageView>(R.id.colorItem)

        fun bind(item: Int) {
            drawableItem.color = item
            drawableItem.strokeColor = Color.WHITE
            drawableItem.strokeWidth = 5F
            imageView.setImageDrawable(drawableItem)

            imageView.setOnClickListener {
                currentPosition?.let {
                    it.strokeWidth = 5F
                }
                currentPosition = drawableItem
                drawableItem.strokeWidth = 10F

                onClickListener(item)
            }
        }


    }
}