package com.example.networking.model

import com.example.networking.delegate.DelegateAdapterItem
import com.example.networking.network.characters.Location
import com.example.networking.network.characters.Origin

class DeadCharacter(
    val id: Int = 0,
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