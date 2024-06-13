package com.midas.restaurant.exception

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.common.dto.response.CommonResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<CommonResponse<List<String>>> {
        log.error(e.message, e)
        val errors = e.allErrors.map { it.defaultMessage ?: "${it.objectName}을 다시 확인 해주세요." }
        return ResponseEntity.badRequest().body(CommonResponse.of(ResultStatus.NOT_VALID_REQUEST, errors))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<CommonResponse<Any>> {
        log.error(e.message)
        return ResponseEntity.internalServerError().body(CommonResponse.of(ResultStatus.UNKNOWN_EXCEPTION))
    }

}