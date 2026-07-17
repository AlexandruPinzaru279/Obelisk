package com.example.idletest.backend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Username - ul este obligatoriu")
    @field:Size(
        min = 3,
        max = 50,
        message = "Username - ul trebuie sa contina intre 3 si 50 caractere"
    )
    val username: String,

    @field:NotBlank(message = "Parola este obligatorie")
    @field:Size(
        min = 8,
        max = 64,
        message = "Parola trebuie sa aiba intre 8 si 64 caractere"
    )
    val password: String
)
