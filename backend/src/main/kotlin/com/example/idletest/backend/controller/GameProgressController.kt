package com.example.idletest.backend.controller

import com.example.idletest.backend.dto.GameProgressDto
import com.example.idletest.backend.service.GameProgressService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/progress")
class GameProgressController (
    private val gameProgressService: GameProgressService
) {
    @GetMapping("/{userId}")
    fun getProgress(@PathVariable userId: Long): GameProgressDto {
        return gameProgressService.getProgress(userId)
    }

    @PutMapping("/{userId}")
    fun saveProgress(
        @PathVariable userId: Long,
        @RequestBody progress: GameProgressDto
    ): GameProgressDto {
        return gameProgressService.saveProgress(userId, progress)
    }
}