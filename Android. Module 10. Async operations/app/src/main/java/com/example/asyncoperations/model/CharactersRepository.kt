package com.example.asyncoperations.model

import com.example.asyncoperations.model.ui.CharacterUI
import kotlinx.coroutines.flow.Flow

interface CharactersRepository  {

    fun observeCharacter(): Flow<List<CharacterUI>>
    suspend fun updateCharacter(): Exception?
}