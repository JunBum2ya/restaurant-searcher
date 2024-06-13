package com.midas.restaurant.review.dto.response

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.review.dto.ReviewDto
import java.time.LocalDateTime

data class SimpleReviewResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {

    companion object {
        fun from(review: ReviewDto): SimpleReviewResponse {
            return SimpleReviewResponse(
                id = review.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                title = review.title,
                content = review.content,
                createdAt = review.createdAt,
                updatedAt = review.createdAt
            )
        }
    }

}