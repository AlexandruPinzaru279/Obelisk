package com.example.idletest.backend.repository

import com.example.idletest.backend.model.UserAccount
import org.springframework.data.jpa.repository.JpaRepository

interface UserAccountRepository : JpaRepository<UserAccount, Long>{

    fun findByUsername(username: String): UserAccount?
    fun existsByUsername(username: String): Boolean
}