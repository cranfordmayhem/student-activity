package com.example.studentactivity.service

import com.example.studentactivity.repository.RefreshTokenRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RefreshTokenCleanupService(private val refreshTokenRepo: RefreshTokenRepository): CommandLineRunner {

    private val logger = LoggerFactory.getLogger(RefreshTokenCleanupService::class.java)

    @Transactional
    fun cleanup(){
        val now = Instant.now()
        val deletedCount = refreshTokenRepo.deleteExpiredOrUsedTokens(now)
        logger.info("Deleted $deletedCount expired or used refresh tokens")
    }

    override fun run(vararg args: String) {
        cleanup()
    }

    @Scheduled(cron="0 */30 * * * *")
    fun scheduledCleanup() {
        logger.info("Running scheduled refresh token cleanup")
        cleanup()
    }
}