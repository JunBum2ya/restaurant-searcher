package com.midas.restaurant.review.dto.response

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.review.dto.ReviewDto
import com.midas.restaurant.review.dto.ReviewDetailDto
import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    val restaurantId: Long,
    val authorName: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(review: ReviewDto, restaurantId: Long, authorName: String): ReviewResponse {
            return ReviewResponse(
                id = review.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                restaurantId = restaurantId,
                authorName = authorName,
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
                authorName = review.author.username,
                title = review.title,
                content = review.content,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt
            )
        }
    }
}
