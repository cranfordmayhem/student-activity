package com.example.studentactivity.dto

import com.example.studentactivity.entity.enums.ActivityType
import jakarta.validation.constraints.NotBlank
import java.time.Instant

data class ActivityRequest(
    val type: ActivityType,

    @field:NotBlank(message="Description is required")
    val description: String
)

data class ActivityResponse(
    val id: Long,
    val type: ActivityType,
    val description: String,
    val timestamp: Instant?
)