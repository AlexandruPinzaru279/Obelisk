package com.example.idletest.backend.config

import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.util.Base64
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtConfig(
    @Value("\${app.jwt.secret}")
    private val encodedSecret: String
) {

    @Bean
    fun jwtSecretKey(): SecretKey {
        val keyBytes = try {
            Base64.getDecoder().decode(encodedSecret)
        } catch (_: IllegalArgumentException) {
            throw IllegalStateException(
                "JWT_SECRET trebuie să fie o valoare Base64 validă"
            )
        }

        require(keyBytes.size >= 32) {
            "JWT_SECRET trebuie să conțină cel puțin 32 de bytes"
        }

        return SecretKeySpec(
            keyBytes,
            "HmacSHA256"
        )
    }

    @Bean
    fun jwtEncoder(secretKey: SecretKey): JwtEncoder {
        val jwkSource: JWKSource<SecurityContext> =
            ImmutableSecret<SecurityContext>(secretKey)

        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun jwtDecoder(secretKey: SecretKey): JwtDecoder {
        return NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
    }
}