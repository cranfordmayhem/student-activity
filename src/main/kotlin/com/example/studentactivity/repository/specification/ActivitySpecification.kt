package com.example.studentactivity.repository.specification

import com.example.studentactivity.dto.ActivityFilter
import com.example.studentactivity.entity.Account
import com.example.studentactivity.entity.Activity
import com.example.studentactivity.entity.enums.ActivityType
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.Instant
import java.time.LocalDate

object ActivitySpecification {
    fun fromFilter(filter: ActivityFilter): Specification<Activity>{
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            predicates += cb.equal(root.get<Account>("account").get<Long>("id"),filter.userId)

            filter.type?.let {
                predicates += cb.equal(root.get<ActivityType>("type"), filter.type)
            }

            filter.exactDate?.let {
                predicates += cb.equal(
                    cb.function("DATE", LocalDate::class.java, root.get<Instant>("createdAt")),
                    it
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}