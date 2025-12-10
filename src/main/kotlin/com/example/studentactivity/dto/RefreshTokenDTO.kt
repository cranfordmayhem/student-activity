package com.example.studentactivity.dto

data class RefreshTokenRequest(
    val token: String
)

data class TokenResponse(
    val accessToken: String
)