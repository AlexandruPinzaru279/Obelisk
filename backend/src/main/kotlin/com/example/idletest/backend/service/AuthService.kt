package com.example.idletest.backend.service

import com.example.idletest.backend.dto.LoginRequest
import com.example.idletest.backend.dto.RegisterRequest
import com.example.idletest.backend.model.UserAccount
import com.example.idletest.backend.repository.UserAccountRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val userAccountRepository: UserAccountRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun register(request: RegisterRequest): UserAccount {
        val normalizedUsername = normalizeUsername(request.username)

        if (userAccountRepository.existsByUsername(normalizedUsername)) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Username - ul este deja utilizat"
            )
        }

        val userAccount = UserAccount(
            username = normalizedUsername,
            passwordHash = passwordEncoder.encode(request.password)
        )

        return try {
            userAccountRepository.saveAndFlush(userAccount)
        } catch (_: DataIntegrityViolationException) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Username - ul este deja utilizat"
            )
        }
    }

    @Transactional(readOnly = true)
    fun authenticate(request: LoginRequest): UserAccount {
        val normalizedUsername = normalizeUsername(request.username)

        val userAccount = userAccountRepository
            .findByUsername(normalizedUsername)
            ?: invalidCredentials()

        val passwordMatches = passwordEncoder.matches(
            request.password,
            userAccount.passwordHash
        )

        if(!passwordMatches) {
            invalidCredentials()
        }

        return userAccount
    }

    private fun normalizeUsername(username: String): String {
        return username.trim().lowercase()
    }

    private fun invalidCredentials(): Nothing {
        throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Username sau parola incorecta"
        )
    }
}