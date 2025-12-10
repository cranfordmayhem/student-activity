package com.example.studentactivity.dto

import jakarta.validation.constraints.*

data class UserAccountRequest(

    @field:NotBlank(message="Name is required")
    val name: String,

    @field:NotBlank(message="Email is required")
    @field:Email(message="Email must be valid")
    val email: String,

    @field:NotBlank(message="Password is required")
    val password: String
)

data class AccountUpdateRequest(
    val name: String?,
    @field:Email(message="Email must be valid")
    val email: String?,
    val password: String?,
)

data class UserAccountResponse(
    val id: Long,
    val name: String,
    val email: String
)