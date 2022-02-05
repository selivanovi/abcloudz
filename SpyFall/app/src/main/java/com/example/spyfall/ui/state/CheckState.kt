package com.example.spyfall.ui.state

sealed class CheckState {

    object SpyWon : CheckState()
    object SpyLost : CheckState()
}