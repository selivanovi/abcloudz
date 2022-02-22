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
    private val spyVoteDirections: SpyVoteDirections
) : VoteViewModel(gameRepository, userRepository) {

    override fun navigateToLocationWon(gameId: String) {
        navigateTo(spyVoteDirections.toLocationWonWithArgs(gameId))
    }

    override fun navigateToSpyWon(gameId: String) {
        navigateTo(spyVoteDirections.toSpyWonWonWithArgs(gameId))
    }
}
