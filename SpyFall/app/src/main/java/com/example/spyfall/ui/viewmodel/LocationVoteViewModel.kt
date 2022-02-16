package com.example.spyfall.ui.viewmodel
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.VoteViewModel
import com.example.spyfall.ui.navigation.LocationVoteDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationVoteViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val locationVoteDirections: LocationVoteDirections
) : VoteViewModel(gameRepository, userRepository) {

    override fun navigateToLocationWonWithArgs(gameId: String) {
        navigateTo(locationVoteDirections.toLocationWonWithArgs(gameId))
    }

    override fun navigateToSpyWonWithArgs(gameId: String) {
        navigateTo(locationVoteDirections.toSpyWonWonWithArgs(gameId))
    }
}