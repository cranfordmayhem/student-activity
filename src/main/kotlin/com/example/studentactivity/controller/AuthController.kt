package com.example.studentactivity.controller

import com.example.studentactivity.dto.*
import com.example.studentactivity.service.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthenticationService,
    private val accountService: AccountService
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    fun register(@Valid @RequestBody userAcc: UserAccountRequest) =
        accountService.create(userAcc)

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginDetails: LoginRequest,
        response: HttpServletResponse
    ) = authService.authenticate(loginDetails, response)

    @GetMapping("/logout")
    fun logout(response: HttpServletResponse) = authService.logout(response)

    @PostMapping("/refresh")
    fun refresh(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): TokenResponse {
        val refreshToken = request.cookies
            ?. firstOrNull { it.name == "refreshToken" }
            ?. value
            ?: throw RuntimeException("Refresh token missing")
        return TokenResponse(accessToken =
            authService.refreshAccessToken(refreshToken, response))
    }
}