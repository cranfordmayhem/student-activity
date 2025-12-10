package com.example.studentactivity.dto

import com.example.studentactivity.entity.enums.ActivityType
import java.time.Instant
import java.time.LocalDate

data class ActivityFilter(
    val userId: Long,
    val exactDate: LocalDate? = null,
    val type: ActivityType? = null
)