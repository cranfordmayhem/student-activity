package com.example.studentactivity.service

import com.example.studentactivity.dto.*
import com.example.studentactivity.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepo: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private val logger = LoggerFactory.getLogger(AccountService::class.java)

    fun create(request: UserAccountRequest): UserAccountResponse? {
        logger.debug("Creating user")
        if(accountRepo.existsByEmail(request.email))
            throw DataIntegrityViolationException("An account with this email already exists.")

        val hashedPass = passwordEncoder.encode(request.password)

        val newUser = UserAccountRequest(
            name = request.name,
            email = request.email,
            password = hashedPass
        )

        return accountRepo.save(newUser.toEntity()).toResponse()
            .also { logger.info("User ${it.id} created successfully") }
    }
}