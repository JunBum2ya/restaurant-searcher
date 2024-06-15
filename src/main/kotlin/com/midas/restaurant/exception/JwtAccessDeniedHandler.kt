package com.midas.restaurant.exception

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler: AccessDeniedHandler {
    private val log = LoggerFactory.getLogger(JwtAccessDeniedHandler::class.java)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.warn("Access denied - {}", accessDeniedException.message)
        accessDeniedException.printStackTrace()
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.")
    }
}