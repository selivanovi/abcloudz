package com.example.spyfall.ui.state

sealed class LobbyState {

    object Play : LobbyState()

    object Wait : LobbyState()
}
