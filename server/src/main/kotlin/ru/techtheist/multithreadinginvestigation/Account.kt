package ru.techtheist.multithreadinginvestigation

import jakarta.persistence.*

@Entity
data class Account (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Version
    var version: Long = 0,

    @Column(nullable = false)
    var balance: Long = 0
)