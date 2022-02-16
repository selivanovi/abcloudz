package com.example.spyfall.ui.state

sealed class VoteState {

    object WaitCurrentPlayer : VoteState()
    object WaitOtherPlayers : VoteState()
    object SpyLost : VoteState()
    object SpyWon : VoteState()
    object GameContinue : VoteState()
}
