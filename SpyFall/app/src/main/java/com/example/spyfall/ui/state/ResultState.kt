package com.example.spyfall.ui.state

import com.example.spyfall.data.entity.Role

sealed class ResultState {

    object Exit : ResultState()
    object HostContinue : ResultState()
    object PlayerContinue : ResultState()
    data class SetRole(val role: Role) : ResultState()
}
