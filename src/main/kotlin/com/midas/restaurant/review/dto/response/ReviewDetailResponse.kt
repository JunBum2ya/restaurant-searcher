package com.midas.restaurant.review.dto.response

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.dto.response.MemberResponse
import com.midas.restaurant.restaurant.dto.response.RestaurantResponse
import com.midas.restaurant.review.dto.ReviewDetailDto
import java.time.LocalDateTime

data class ReviewDetailResponse(
    val id: Long,
    val restaurant: RestaurantResponse,
    val author: MemberResponse,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {

    companion object {
        fun from(review: ReviewDetailDto): ReviewDetailResponse {
            return ReviewDetailResponse(
                id = review.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                restaurant = RestaurantResponse.from(review.restaurant),
                author = MemberResponse.from(review.author),
                title = review.title,
                content = review.content,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt
            )
        }
    }

}