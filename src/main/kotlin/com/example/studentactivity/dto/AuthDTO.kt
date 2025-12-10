package com.example.studentactivity.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message="Username is required")
    @field:Email(message="Username must be a valid email")
    val username: String,

    @field:NotBlank(message="Password is required")
    val password: String
)

data class LoginResponse(
    val id: Long,
    val name: String,
    val username: String,
)