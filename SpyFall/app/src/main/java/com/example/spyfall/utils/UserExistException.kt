package com.example.spyfall.utils

class UserExistException(name: String) : Exception("User already exists: $name"){
}