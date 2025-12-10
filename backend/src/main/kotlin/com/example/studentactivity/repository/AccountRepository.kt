package com.example.studentactivity.repository

import com.example.studentactivity.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<Account, Long>{
    fun findByEmail(email: String): Account?
    fun existsByEmail(email: String): Boolean
}