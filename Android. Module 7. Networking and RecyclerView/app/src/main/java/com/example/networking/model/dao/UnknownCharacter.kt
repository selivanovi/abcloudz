package com.example.networking.model.dao

import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.network.characters.Origin

data class UnknownCharacter(
    val id: Int = 0,
    val image: String = "",
    val name: String = "",
    val origin: Origin = Origin(),
    val species: String = "",
    val status: String = "",
    val episode: List<String> = listOf(),
) : DelegateAdapterItem {

    override fun id(): Int = id

    override fun compareContent(item: DelegateAdapterItem): Boolean =
        this == item
}