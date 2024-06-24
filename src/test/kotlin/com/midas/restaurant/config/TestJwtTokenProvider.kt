package com.midas.restaurant.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.common.component.JwtTokenProvider
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.member.dto.MemberDto
import org.springframework.boot.test.context.TestComponent
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.LocalDateTime

class TestJwtTokenProvider(private val objectMapper: ObjectMapper) :
    JwtTokenProvider(secret = "1234", tokenValidityInSeconds = 1L, objectMapper = objectMapper) {

    override fun afterPropertiesSet() {
        return
    }

    override fun generateToken(member: MemberDto): String {
        return "JWT token"
    }

    override fun extractAuthentication(token: String): Authentication {
        if(token.isBlank()) {
            return UsernamePasswordAuthenticationToken("", "")
        }
        val details = MemberDetails(
            id = 1L,
            username = "testUser",
            password = "testPassword",
            email = "test@test.com",
            expiredDate = LocalDateTime.now().plusDays(1L),
            authorities = mutableListOf(SimpleGrantedAuthority("BASIC"))
        )
        return UsernamePasswordAuthenticationToken(details, details.password, details.authorities)
    }

}