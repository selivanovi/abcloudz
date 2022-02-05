package com.example.spyfall.ui.state

sealed class ResultState {

    object Exit : ResultState()
    object HostContinue : ResultState()
    object PlayerContinue : ResultState()
}