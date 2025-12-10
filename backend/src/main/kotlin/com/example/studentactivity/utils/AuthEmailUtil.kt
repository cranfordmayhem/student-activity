package com.example.studentactivity.utils

import com.example.studentactivity.entity.Account
import com.example.studentactivity.exception.UnauthenticatedException
import com.example.studentactivity.exception.UnauthorizedException
import com.example.studentactivity.exception.UserNotFoundException
import com.example.studentactivity.repository.AccountRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthEmailUtil(
    private val accountRepo: AccountRepository
) {
    fun checkUser(userEmail: String): Account {
        return accountRepo.findByEmail(userEmail)
            ?: throw UserNotFoundException(userEmail)
    }

    fun checkAndVerifyUser(processedEmail: String, userEmail: String, action: String, entity: String): Account {
        val user = accountRepo.findByEmail(userEmail)
            ?: throw UserNotFoundException(userEmail)
        if(processedEmail != user.email)
            throw UnauthorizedException(entity, action)
        return user
    }

    fun getUser(): String {
        val email = SecurityContextHolder.getContext().authentication.name
            ?: UnauthenticatedException()
        return email.toString()
    }
}