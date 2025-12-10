package com.example.studentactivity.dto

import com.example.studentactivity.entity.*
import java.time.Instant

fun UserAccountRequest.toEntity(): Account = Account(
    name = this.name,
    email = this.email,
    password = this.password
)

fun AccountUpdateRequest.toEntity(existing: Account): Account = existing.copy(
    name = this.name ?: existing.name,
    email = this.email ?: existing.email,
    password = this.password ?: existing.password
)

fun Account.toResponse(): UserAccountResponse = UserAccountResponse(
    id = this.id,
    name = this.name,
    email = this.email
)
fun RefreshTokenRequest.toEntity(user: Account, expiryDate: Instant):
        RefreshToken = RefreshToken(
    token = this.token,
    expiryDate = expiryDate,
    userAccount = user
)

fun ActivityRequest.toEntity(account: Account): Activity = Activity(
    type = this.type,
    description = this.description,
    account = account
)

fun Activity.toResponse(): ActivityResponse = ActivityResponse(
    id = this.id,
    type = this.type,
    description = this.description,
    timestamp = this.updatedAt
)