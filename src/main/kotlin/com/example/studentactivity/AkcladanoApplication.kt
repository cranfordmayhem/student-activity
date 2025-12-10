package com.example.studentactivity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "springAuditorAware")
@EnableScheduling
class AkcladanoApplication

fun main(args: Array<String>) {
	runApplication<AkcladanoApplication>(*args)
}
