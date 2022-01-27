package com.example.spyfall.domain.entity

import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role

data class PlayerDomain(
    val playerId: String,
    val name: String?,
    val status: PlayerStatus?,
    var role: Role?
)