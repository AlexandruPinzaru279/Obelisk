package com.example.idletest.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_accounts")
class UserAccount(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(
        nullable = false,
        unique = true,
        length = 50
    )
    var username: String = "",

    @Column(
        nullable = false,
        length = 255
    )
    var passwordHash: String = ""
)