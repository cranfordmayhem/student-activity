package com.example.studentactivity.dto

import com.example.studentactivity.entity.enums.ActivityType
import java.time.Instant

data class ActivityRequest(
    val type: ActivityType,
    val description: String
)

data class ActivityResponse(
    val id: Long,
    val type: ActivityType,
    val description: String,
    val timestamp: Instant?
)