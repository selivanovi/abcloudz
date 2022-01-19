package com.example.spyfall.domain.usecase

import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.domain.utils.Constants
import com.example.spyfall.domain.utils.InvalidNameException
import javax.inject.Inject

class SetNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun execute(name: String) {

        val names = userRepository.getUsers()

        if (names == null) {
            userRepository.addUser(name)
        }
        else {
            if (!names.contains(name)) userRepository.addUser(name)
            else throw InvalidNameException(Constants.INVALID_NAME_EXCEPTION)
        }
    }
}