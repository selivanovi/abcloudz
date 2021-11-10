package com.example.localdatastorage.utils

/**
 * These function validate email and password we using in LoginViewModels
 */

fun validateEmail(email: String): Boolean {
    return email.contains("@") && email.contains(".")
}

fun validatePassword(password: String): Boolean {
    return password.length >= 8
}