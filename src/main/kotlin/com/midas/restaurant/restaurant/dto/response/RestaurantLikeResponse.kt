package com.midas.restaurant.restaurant.dto.response

import com.midas.restaurant.restaurant.dto.RestaurantLikeDto

data class RestaurantLikeResponse(val restaurantId: Long, val stars: Float) {
    companion object {
        fun from(restaurantId: Long, restaurantLikeDto: RestaurantLikeDto) =
            RestaurantLikeResponse(restaurantId = restaurantId, stars = restaurantLikeDto.stars)
    }
}
