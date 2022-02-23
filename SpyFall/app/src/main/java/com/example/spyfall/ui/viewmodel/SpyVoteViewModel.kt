package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.VoteViewModel
import com.example.spyfall.ui.navigation.SpyVoteDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpyVoteViewModel @Inject constructor(
    gameRepository: GameRepository,
    userRepository: UserRepository,
) : VoteViewModel(gameRepository, userRepository) {

    override fun navigateToLocationWon(gameId: String) {
        navigateTo(SpyVoteDirections.toLocationWonWithArgs(gameId))
    }

    override fun navigateToSpyWon(gameId: String) {
        navigateTo(SpyVoteDirections.toSpyWonWonWithArgs(gameId))
    }
}
