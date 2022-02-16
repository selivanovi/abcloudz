package com.example.spyfall.ui.state

import com.example.spyfall.data.entity.Role

sealed class RoleState {

    data class SetRole(val role: Role) : RoleState()
    data class SetTime(val time: Long) : RoleState()
    object Voted : RoleState()
    object VoteSpy : RoleState()
    object VotePlayer : RoleState()
    object LocationSpy : RoleState()
    object LocationPlayer : RoleState()
    object Spy : RoleState()
    object Player : RoleState()
    object GameIsPlaying : RoleState()
    object GameIsPause : RoleState()
}
