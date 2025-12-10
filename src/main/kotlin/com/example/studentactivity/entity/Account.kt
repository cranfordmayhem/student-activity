package com.example.studentactivity.entity

import jakarta.persistence.*

@Entity
@Table(name="account")
data class Account(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable=false, length=255)
    var name: String,

    @Column(nullable=false, unique=true, length=255)
    var email: String,

    @Column(nullable=false, length=255)
    var password: String
): Auditor()