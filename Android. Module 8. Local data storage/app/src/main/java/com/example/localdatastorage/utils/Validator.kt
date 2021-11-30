package com.example.localdatastorage.utils

import android.util.Patterns

/**
 * These function validate email and password we using in LoginViewModels
 */

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidPassword() = !isNullOrEmpty() && this.length >= 8