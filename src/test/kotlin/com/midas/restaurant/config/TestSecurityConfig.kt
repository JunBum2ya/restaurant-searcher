package com.midas.restaurant.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.common.component.JwtAuthenticationEntryPoint
import com.midas.restaurant.common.component.JwtTokenProvider
import com.midas.restaurant.common.filter.JwtTokenFilter
import com.midas.restaurant.exception.JwtAccessDeniedHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@TestConfiguration
@Import(SecurityConfig::class, JwtAuthenticationEntryPoint::class, ObjectMapper::class, JwtAccessDeniedHandler::class)
class TestSecurityConfig {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Bean
    fun jwtTokenProvider(): JwtTokenProvider {
        return TestJwtTokenProvider(objectMapper)
    }

}