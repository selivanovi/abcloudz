package com.example.asyncoperations.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.asyncoperations.model.ui.CharacterUI

class CharacterComparator(
    private val oldList: MutableList<CharacterUI>,
    private val newList: List<CharacterUI>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.idCharacter == newUser.idCharacter
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.name == newUser.name && oldUser.image == newUser.image &&
                oldUser.species == newUser.species && oldUser.status == newUser.status

    }
}
