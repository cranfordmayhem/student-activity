package com.example.studentactivity.repository

import com.example.studentactivity.entity.Activity
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivityRepository: JpaRepository<Activity, Long>{
    fun findByAccountId(accountId: Long, pageable: Pageable): Page<Activity>
}