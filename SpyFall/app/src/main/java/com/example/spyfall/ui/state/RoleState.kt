package com.example.spyfall.ui.state

sealed class RoleState {

    object Voted : RoleState()
    object VoteSpy : RoleState()
    object VotePlayer : RoleState()
    object LocationSpy : RoleState()
    object LocationPlayer : RoleState()
    object Spy : RoleState()
    object Player : RoleState()
    object GameIsPlaying : RoleState()
    object GameIsPause : RoleState()
    object Exit : RoleState()
}