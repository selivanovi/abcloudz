package com.example.spyfall.ui.navigation

import androidx.navigation.NavDirections

sealed class NavEvent {

    data class To(val navDirections: NavDirections) : NavEvent()
    object Back : NavEvent()
    object Up : NavEvent()
}