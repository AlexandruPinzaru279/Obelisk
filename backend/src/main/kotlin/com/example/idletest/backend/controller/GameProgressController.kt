package com.example.idletest.backend.controller

import com.example.idletest.backend.dto.GameProgressDto
import com.example.idletest.backend.service.GameProgressService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/progress")
class GameProgressController (
    private val gameProgressService: GameProgressService
) {
    @GetMapping("/me")
    fun getProgress(
        @AuthenticationPrincipal jwt: Jwt?
    ): GameProgressDto {
        val userId = extractUserId(jwt)

        return gameProgressService.getProgress(userId)
    }

    @PutMapping("/me")
    fun saveProgress(
        @AuthenticationPrincipal jwt: Jwt?,
        @RequestBody progress: GameProgressDto
    ): GameProgressDto {
        val userId = extractUserId(jwt)

        return gameProgressService.saveProgress(
            userId = userId,
            dto = progress)
    }

    private fun extractUserId(jwt: Jwt?): Long {
        return jwt
            ?.subject
            ?.toLongOrNull()
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Tokenul nu contine un user ID valid."
            )
    }
}