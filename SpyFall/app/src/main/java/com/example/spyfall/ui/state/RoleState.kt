package com.example.spyfall.ui.state


sealed class RoleState {

    object Voted : RoleState()
    object Spy : RoleState()
    object Player : RoleState()
    object GameIsPlaying : RoleState()
    object GameIsPause : RoleState()
}
