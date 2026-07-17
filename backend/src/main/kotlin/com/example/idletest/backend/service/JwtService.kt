package com.example.idletest.backend.service

import com.example.idletest.backend.model.UserAccount
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class JwtService(
    private val jwtEncoder: JwtEncoder,

    @Value("\${app.jwt.expiration-seconds}")
    private val expirationSeconds: Long
) {
    fun generateToken(userAccount: UserAccount): String {
        val userId = requireNotNull(userAccount.id) {
            "contul trebuie salvat inainte de generarea tokenului"
        }

        val now = Instant.now()

        val claims = JwtClaimsSet.builder()
            .issuer("idletest-backend")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expirationSeconds))
            .subject(userId.toString())
            .claim("username", userAccount.username)
            .build()

        val header = JwsHeader
            .with(MacAlgorithm.HS256)
            .type("JWT")
            .build()

        return jwtEncoder
            .encode(
                JwtEncoderParameters.from(
                    header,
                    claims
                )
            )
            .tokenValue
    }
}