package com.example.asyncoperations.model

import android.util.Log
import com.example.asyncoperations.model.ui.CharacterUI
import com.example.asyncoperations.utils.ConnectingException
import com.example.asyncoperations.utils.toDTO
import com.example.asyncoperations.utils.toUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharactersRepositoryImpl(
    private val remoteSource: RemoteDataSource,
    private val localSource: LocalDataSource
) : CharactersRepository {


    override fun observeCharacter(): Flow<List<CharacterUI>> =
        localSource.getCharacters().map { list -> list.map { it.toUI() } }

    override suspend fun updateCharacter() {
        Log.d("CharactersRepository", "updateCharacter")
        val request = remoteSource.getCharactersByIds(CHARACTERS_IDS)
        request.exception?.let {
            throw ConnectingException()
        }
        Log.d("CharactersRepository", "$request")
        request.body?.let { list ->
            localSource.insertCharacters(list.map { it.toDTO() })
        }
    }

    companion object {
        private val CHARACTERS_IDS = Array(50) { i -> i }.contentToString()
    }
}