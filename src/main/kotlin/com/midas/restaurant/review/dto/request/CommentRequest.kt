package com.midas.restaurant.review.dto.request

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.review.dto.CommentDto
import jakarta.validation.constraints.NotEmpty

data class CommentRequest(@field:NotEmpty(message = "댓글을 입력하세요.") val content: String?) {

    constructor() : this(content = null)

    fun toDto(): CommentDto {
        return CommentDto(content = content ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST))
    }
}