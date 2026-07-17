package com.example.idletest.backend.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Username - ul este obligatoriu")
    val username: String,

    @field:NotBlank(message = "Parla este obligatorie")
    val password: String
)
