package com.midas.restaurant.exception

import com.midas.restaurant.common.dto.response.CommonResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    private val log = LoggerFactory.getLogger(CustomExceptionHandler::class.java)

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<CommonResponse<Any>> {
        log.error(e.message, e)
        return ResponseEntity.status(e.status.status).body(CommonResponse.of(code = e.code, message = e.message))
    }

}