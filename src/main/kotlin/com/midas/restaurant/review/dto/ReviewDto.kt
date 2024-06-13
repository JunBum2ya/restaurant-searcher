package com.midas.restaurant.review.dto

import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.review.domain.Review
import java.time.LocalDateTime

class ReviewDto(
    val id: Long? = null,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {

    companion object {
        fun from(review: Review): ReviewDto {
            return ReviewDto(
                id = review.id,
                title = review.getTitle(),
                content = review.getContent(),
                createdAt = review.getCreatedAt(),
                updatedAt = review.getUpdatedAt()
            )
        }
    }

    fun toEntity(restaurant: Restaurant, author: Member) =
        Review(id = id, restaurant = restaurant, author = author, title = title, content = content)

}