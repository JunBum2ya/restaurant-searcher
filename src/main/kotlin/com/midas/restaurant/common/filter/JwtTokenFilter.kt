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

class JwtTokenFilter(private val tokenProvider: JwtTokenProvider): OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtTokenFilter::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Token is missing in header")
            filterChain.doFilter(request, response)
            return
        }
        try {
            val token = header.split(" ")[1].trim()
            val authentication = tokenProvider.extractAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }catch (e: RuntimeException) {
            log.error("Error occurs during authentication request - {}", e.message)
            filterChain.doFilter(request, response)
        }
    }
}