package com.example.studentactivity.entity

import com.example.studentactivity.entity.enums.ActivityType
import jakarta.persistence.*

@Entity
@Table(name="activity")
data class Activity(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    var type: ActivityType,

    @Column(nullable=false, length=255)
    var description: String,

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="account_id", nullable=false)
    val account: Account
) : Auditor()