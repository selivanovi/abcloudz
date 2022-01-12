package com.example.camera.recyclerviews

import android.graphics.Bitmap
import android.net.Uri
import android.opengl.GLSurfaceView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.camera.Filter
import com.example.camera.R
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class FiltersAdapter(val onClickListener: (Bitmap) -> Unit, var bitmap: Bitmap?) :
    RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

    private val filtersList = mutableListOf<Bitmap>()

    fun setData(list: List<Bitmap>) {
        with(filtersList) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageBitmap(filtersList[position])
        holder.imageView.setOnClickListener {
            onClickListener(filtersList[position])
        }
    }

    override fun getItemCount(): Int {
        return filtersList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById<ImageView>(R.id.filterItem)
    }
}