package com.midas.restaurant.review.dto

import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.review.domain.Review
import java.time.LocalDateTime

class ReviewDetailDto(
    val id: Long? = null,
    val restaurant: RestaurantDto,
    val author: MemberDto,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {

    companion object {
        fun from(review: Review): ReviewDetailDto {
            return ReviewDetailDto(
                id = review.id,
                restaurant = RestaurantDto.from(review.restaurant),
                author = MemberDto(review.author),
                title = review.getTitle(),
                content = review.getContent(),
                createdAt = review.getCreatedAt(),
                updatedAt = review.getUpdatedAt()
            )
        }
    }

}