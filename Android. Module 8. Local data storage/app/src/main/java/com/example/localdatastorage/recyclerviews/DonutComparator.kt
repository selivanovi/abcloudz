package com.example.localdatastorage.recyclerviews

import androidx.recyclerview.widget.DiffUtil
import com.example.localdatastorage.model.entities.ui.DonutUI

class DonutComparator(
    private val oldList: List<DonutUI>,
    private val newList: List<DonutUI>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.id == newUser.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.name == newUser.name && oldUser.type == newUser.type &&
                oldUser.ppu == newUser.ppu && oldUser.allergy == newUser.allergy &&
                newUser.getBattersString("\n") == oldUser.getBattersString("\n") &&
                newUser.getToppingsString("\n") == oldUser.getToppingsString("\n")
    }
}