package com.example.studentactivity.service

import com.example.studentactivity.dto.*
import com.example.studentactivity.exception.AuthUserException
import com.example.studentactivity.exception.InvalidTokenException
import com.example.studentactivity.exception.UserNotFoundException
import com.example.studentactivity.repository.*
import com.example.studentactivity.utils.*
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date
import java.util.UUID

@Service
class AuthenticationService(
    private val userAccountRepo: AccountRepository,
    private val authManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService,
    private val refreshTokenRepo: RefreshTokenRepository,
    private val refreshTokenUtil: RefreshTokenUtil,
    private val tokenToCookieUtil: TokenToCookieUtil,
    @Value("\${jwt.accessTokenExpiration}") private val accessTokenExpiration: Long = 0,
    @Value("\${jwt.refreshTokenExpiration}") private val refreshTokenExpiration: Long = 0
) {
    private val logger = LoggerFactory.getLogger(AuthenticationService::class.java)

    fun authenticate(loginRequest: LoginRequest, response: HttpServletResponse): LoginResponse {
        try {
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
        } catch(ex: Exception) {
            logger.error("Login Failed: ${ex.message}")
            throw AuthUserException()
        }

        val user = userDetailsService.loadUserByUsername(loginRequest.username)
        val entityUser = userAccountRepo.findByEmail(user.username)
            ?: throw UserNotFoundException(user.username)

        val accessToken = createAccessToken(user)
        val rawRefreshToken = createRefreshToken(user)
        val hashedRefreshToken = refreshTokenUtil.hash(rawRefreshToken)

        val refreshTokenEntity = RefreshTokenRequest(hashedRefreshToken).toEntity(
            entityUser,
            Instant.now().plusMillis(refreshTokenExpiration)
        ).apply {
            createdBy = entityUser.email
        }
        refreshTokenRepo.save(refreshTokenEntity)

        tokenToCookieUtil.toHeader(
            "accessToken", accessToken,
            accessTokenExpiration, response
        )
        tokenToCookieUtil.toHeader(
            "refreshToken", rawRefreshToken,
            refreshTokenExpiration, response
        )

        return LoginResponse(entityUser.id, entityUser.name, user.username)
    }

    fun refreshAccessToken(rawRefreshToken: String, response: HttpServletResponse): String {
        val username = tokenService.extractUsername(rawRefreshToken)
        val hashedToken = refreshTokenUtil.hash(rawRefreshToken)

        val refreshTokenEntity = refreshTokenRepo.findByToken(hashedToken)
            .orElseThrow { InvalidTokenException("REFRESH_TOKEN_NOT_FOUND") }

        refreshTokenUtil.isTokenValid(refreshTokenEntity)

        val currentUserDetails = userDetailsService.loadUserByUsername(username)
        if(currentUserDetails.username != refreshTokenEntity.userAccount.email) {
            throw InvalidTokenException("INVALID_SIGNATURE")
        }

        refreshTokenEntity.used = true
        refreshTokenRepo.save(refreshTokenEntity)

        val newRefreshToken = createRefreshToken(currentUserDetails)
        refreshTokenUtil.regenerateAndSaveRefreshToken(refreshTokenEntity, newRefreshToken, refreshTokenExpiration)
        val newAccessToken = createAccessToken(currentUserDetails)

        tokenToCookieUtil.toHeader("accessToken", newAccessToken,
            accessTokenExpiration, response)
        tokenToCookieUtil.toHeader("refreshToken", newRefreshToken,
            refreshTokenExpiration, response)

        return newAccessToken
    }

    fun logout(response: HttpServletResponse): String {
        tokenToCookieUtil.clearCookies(response)
        return "Logged out successfully"
    }

    private fun createAccessToken(user: UserDetails): String {
        return tokenService.generateToken(
            subject = user.username,
            expiration = Date(System.currentTimeMillis() + accessTokenExpiration),
            additionalClaims = mapOf("nonce" to UUID.randomUUID().toString())
        )
    }

    private fun createRefreshToken(user: UserDetails) = tokenService.generateToken(
        subject = user.username,
        expiration = Date(System.currentTimeMillis() + refreshTokenExpiration),
        additionalClaims = mapOf("nonce" to UUID.randomUUID().toString())
    )
}