package com.midas.restaurant.review.dto.response

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.review.dto.ReviewDto
import com.midas.restaurant.review.dto.ReviewDetailDto
import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    val restaurantId: Long,
    val authorId: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(review: ReviewDto, restaurantId: Long, authorId: Long): ReviewResponse {
            return ReviewResponse(
                id = review.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                restaurantId = restaurantId,
                authorId = authorId,
                title = review.title,
                content = review.content,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt
            )
        }

        fun from(review: ReviewDetailDto): ReviewResponse {
            return ReviewResponse(
                id = review.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                restaurantId = review.restaurant.id?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                authorId = review.author.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                title = review.title,
                content = review.content,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt
            )
        }
    }
}
