package com.example.studentactivity.service

import com.example.studentactivity.dto.*
import com.example.studentactivity.entity.enums.ActivityType
import com.example.studentactivity.exception.IdNotFoundException
import com.example.studentactivity.repository.ActivityRepository
import com.example.studentactivity.repository.specification.ActivitySpecification
import com.example.studentactivity.utils.AuthEmailUtil
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate

@Service
class ActivityService(
    private val actRepo: ActivityRepository,
    private val authEmail: AuthEmailUtil
) {

    private val logger = LoggerFactory.getLogger(ActivityService::class.java)

    fun create(request: ActivityRequest, userEmail: String): ActivityResponse {
        val user = authEmail.checkUser(userEmail)

        return actRepo.save(request.toEntity(user)).toResponse()
    }
    fun retrieveByUser(
        type: ActivityType?,
        exactDate: LocalDate?, pageable: Pageable, userEmail: String)
    : Page<ActivityResponse> {
        val user = authEmail.checkUser(userEmail)
        val filter = ActivityFilter(user.id, exactDate, type)
        return actRepo.findAll(ActivitySpecification.fromFilter(filter), pageable).map {
            it.toResponse()
        }
    }

    fun retrieveById(id: Long, userEmail: String): ActivityResponse =
        actRepo.findByIdOrNull(id)?.apply{
            val user = authEmail.checkAndVerifyUser(
                this.account.email, userEmail,
                "retrieve", "Activity"
            )
        }?.toResponse() ?: throw IdNotFoundException(id, "Activity")

    fun update(id: Long, request: ActivityRequest, userEmail: String): ActivityResponse =
        actRepo.findByIdOrNull(id)?.apply{
            authEmail.checkAndVerifyUser(
                this.account.email, userEmail,
                "update", "Activity"
            )
        }?.let { existing ->
            val updated = existing.copy(
                type = request.type,
                description = request.description
            )
            actRepo.save(updated).toResponse()
        } ?: throw IdNotFoundException(id, "Activity")

    fun delete(id: Long, userEmail: String) =
        actRepo.findByIdOrNull(id)?.let{
            authEmail.checkAndVerifyUser(
                it.account.email, userEmail,
                "delete", "Activity"
            )
            actRepo.deleteById(id)
        } ?: throw IdNotFoundException(id, "Activity")
}