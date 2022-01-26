package com.example.spyfall.data.entity

import com.example.spyfall.utils.generateRandomID

data class User(
    val userId: String = generateRandomID(),
    val name: String
)
