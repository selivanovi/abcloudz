package com.example.spyfall.domain.entity

import com.example.spyfall.utils.generateRandomId
import java.io.Serializable

data class UserDomain(
    val userId: String = generateRandomId(),
    val name: String
) : Serializable
