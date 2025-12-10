package com.example.studentactivity.service

import com.example.studentactivity.exception.UserNotFoundException
import com.example.studentactivity.repository.AccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
    private val userAccountRepo: AccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userAccountRepo.findByEmail(username)?.let { user ->
            User.builder()
                .username(user.email)
                .password(user.password)
                .build()
        } ?: throw UserNotFoundException(username)
}