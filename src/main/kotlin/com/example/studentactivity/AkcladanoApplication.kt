package com.example.studentactivity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "springAuditorAware")
class AkcladanoApplication

fun main(args: Array<String>) {
	runApplication<AkcladanoApplication>(*args)
}
