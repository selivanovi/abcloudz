package com.example.spyfall.ui.state

sealed class GameState {

    object ExitToMenu : GameState()
    object ExitToLobbyForHost : GameState()
    object ExitToLobbyForPlayer : GameState()
}