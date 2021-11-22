package com.example.localdatastorage.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.localdatastorage.R
import com.example.localdatastorage.model.entities.ui.DonutUI

class ListAdapter(
    var onLongClickListener: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private val _list = mutableListOf<DonutUI>()

    fun setData(list: List<DonutUI>) {
        val diffUtil = DonutComparator(_list, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        with(_list) {
            clear()
            addAll(list)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val ppuTextView = view.findViewById<TextView>(R.id.ppuTextView)
        private val typeTextView = view.findViewById<TextView>(R.id.typeTextView)
        private val batterTextView = view.findViewById<TextView>(R.id.batterTextView)
        private val toppingTextView = view.findViewById<TextView>(R.id.toppingTextView)


        fun bind(item: DonutUI) {
            nameTextView.text = item.name
            ppuTextView.text = itemView.resources.getString(R.string.ppu_item_List, item.ppu.toString())
            typeTextView.text = itemView.resources.getString(R.string.type_item_list, item.type)
            batterTextView.text = itemView.resources.getString(R.string.batter_item_list, item.getBattersString("\n"))
            toppingTextView.text = itemView.resources.getString(R.string.topping_item_list, item.getToppingsString("\n"))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_donut_layout, parent, false)
        )


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(_list[position])

        onLongClickListener?.let { onLongClickListener ->
            holder.itemView.setOnLongClickListener {
                onLongClickListener.invoke(_list[position].id)
                true
            }
        }

    }

    override fun getItemCount(): Int {
        return _list.size
    }
}