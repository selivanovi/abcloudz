package com.example.networking.model.dao

import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.network.characters.Origin

data class AliveCharacter(
    override val id: Int? = null,
    val image: String? = null,
    val name: String? = null,
    val origin: Origin? = null,
    val species: String? = null,
    val status: String? = null,
    val episode: List<String>? = null,
) : DelegateAdapterItem {

    override fun compareContent(item: DelegateAdapterItem): Boolean =
        if (item is AliveCharacter) {
            item.image == this.image &&
                    item.origin == this.origin &&
                    item.species == this.species &&
                    item.status == this.status &&
                    item.episode == item.episode

        } else false
}