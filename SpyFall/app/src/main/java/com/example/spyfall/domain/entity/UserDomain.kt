package com.example.spyfall.domain.entity

import com.example.spyfall.utils.generateRandomId

data class UserDomain(
    val userId: String = generateRandomId(),
    val name: String
)
