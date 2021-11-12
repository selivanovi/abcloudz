package com.example.networking.utils

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.networking.model.dao.Character
import com.example.networking.model.dao.Episode
import com.example.networking.model.network.characters.CharacterResponse
import com.example.networking.model.network.episodes.EpisodeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun EpisodeResponse.toDTO() =
    Episode(
        id = this.id,
        name = this.name,
        episode = this.episode,
        airData = this.air_date
    )

fun CharacterResponse.toDTO(): Character =
    Character(
        id = this.id,
        name = this.name,
        origin = this.origin,
        episode = this.episode,
        image = this.image
    )

fun <T> Flow<T>.launchWhenStarted(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}