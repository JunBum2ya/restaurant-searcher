package com.midas.restaurant.restaurant.dto.request

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.restaurant.dto.RestaurantLikeDto
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class RestaurantLikeRequest(
    @field:Pattern(regexp = "^(0*[0-4](\\.[0-9])?|5(\\.0)?)$", message = "별점을 입력하세요.") val stars: String?
) {
    fun getStars(): Float {
        return stars?.toFloatOrNull() ?: 0f
    }

    fun toDto(): RestaurantLikeDto {
        return RestaurantLikeDto(
            stars = stars?.toFloatOrNull() ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST)
        )
    }
}
