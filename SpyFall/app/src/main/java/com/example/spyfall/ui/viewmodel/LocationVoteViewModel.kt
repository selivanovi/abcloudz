package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.VoteViewModel
import com.example.spyfall.ui.navigation.LocationVoteDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationVoteViewModel @Inject constructor(
    gameRepository: GameRepository,
    userRepository: UserRepository,
) : VoteViewModel(gameRepository, userRepository) {

    override fun navigateToLocationWon(gameId: String) {
        navigateTo(LocationVoteDirections.toLocationWonWithArgs(gameId))
    }

    override fun navigateToSpyWon(gameId: String) {
        navigateTo(LocationVoteDirections.toSpyWonWonWithArgs(gameId))
    }
}
