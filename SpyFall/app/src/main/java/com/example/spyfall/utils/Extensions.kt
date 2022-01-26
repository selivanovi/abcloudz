package com.example.spyfall.utils

import java.util.*

fun generateRandomID(): String =
    UUID.randomUUID().toString().takeLast(5)