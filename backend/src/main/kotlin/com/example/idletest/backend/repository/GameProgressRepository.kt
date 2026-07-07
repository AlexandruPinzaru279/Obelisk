package com.example.idletest.backend.repository

import com.example.idletest.backend.model.GameProgress
import org.springframework.data.jpa.repository.JpaRepository

interface GameProgressRepository : JpaRepository<GameProgress, Long> {
    fun findByUserId(userId: Long): GameProgress?
}