package com.midas.restaurant.review.dto.request

import com.midas.restaurant.review.dto.ReviewDto
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ReviewRequest(
    @field:NotNull(message = "음식점 아이디를 입력하세요.") val restaurantId: Long,
    @field:NotEmpty(message = "제목이 없습니다.") val title: String,
    @field:NotEmpty(message = "본문이 없습니다.") val content: String
) {

    fun toDto(): ReviewDto = ReviewDto(title = title, content = content)

}