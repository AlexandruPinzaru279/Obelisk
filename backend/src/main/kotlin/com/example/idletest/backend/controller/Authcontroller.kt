package com.example.idletest.backend.controller

import com.example.idletest.backend.dto.AuthResponse
import com.example.idletest.backend.dto.LoginRequest
import com.example.idletest.backend.dto.RegisterRequest
import com.example.idletest.backend.service.AuthService
import com.example.idletest.backend.service.JwtService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtService
) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest
    ): ResponseEntity<AuthResponse> {
        val userAccount = authService.register(request)
        val token = jwtService.generateToken(userAccount)

        val response = AuthResponse(
            token = token,
            username = userAccount.username
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<AuthResponse> {
        val userAccount = authService.authenticate(request)
        val token = jwtService.generateToken(userAccount)

        val response = AuthResponse(
            token = token,
            username = userAccount.username
        )

        return ResponseEntity.ok(response)
    }
}