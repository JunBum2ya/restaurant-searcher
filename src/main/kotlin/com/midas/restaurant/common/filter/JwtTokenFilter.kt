package com.midas.restaurant.common.filter

import com.midas.restaurant.common.component.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.jvm.Throws

class JwtTokenFilter(private val tokenProvider: JwtTokenProvider) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtTokenFilter::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            resolveToken(request)?.let {
                val authentication = tokenProvider.extractAuthentication(it)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: RuntimeException) {
            log.error("Error occurs during authentication request - {}", e.message)
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (header == null || !header.startsWith("Bearer ")) {
            log.error("Token is missing in header")
            null
        } else {
            header.substring(7).trim()
        }

    }
}