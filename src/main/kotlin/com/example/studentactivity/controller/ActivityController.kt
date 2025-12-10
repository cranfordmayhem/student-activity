package com.example.studentactivity.controller

import com.example.studentactivity.dto.*
import com.example.studentactivity.service.ActivityService
import com.example.studentactivity.utils.AuthEmailUtil
import jakarta.validation.Valid
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/activity")
class ActivityController(
    private val actService: ActivityService,
    private val authEmail: AuthEmailUtil
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createActivity(@Valid @RequestBody request: ActivityRequest) =
        authEmail.getUser().let{
            actService.create(request, it)
        }

    @GetMapping
    fun getAllActivities(pageable: Pageable) =
        authEmail.getUser().let {
            actService.retrieveByUser(pageable, it)
        }

    @GetMapping("/{id}")
    fun getActivityById(@PathVariable id: Long) =
        authEmail.getUser().let {
            actService.retrieveById(id, it)
        }

    @PutMapping("/{id}")
    fun updateActivity(@PathVariable id: Long, @Valid @RequestBody request: ActivityRequest) =
        authEmail.getUser().let {
            actService.update(id, request, it)
        }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteActivity(@PathVariable id: Long) =
        authEmail.getUser().let {
            actService.delete(id, it)
        }
}