package com.midas.restaurant.restaurant.dto

import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.domain.RestaurantLike

class RestaurantLikeDto(val stars: Float) {
    companion object {
        fun from(restaurantLike: RestaurantLike): RestaurantLikeDto {
            return RestaurantLikeDto(stars = restaurantLike.stars)
        }
    }

    fun toEntity(restaurant: Restaurant, member: Member): RestaurantLike {
        return RestaurantLike(restaurant = restaurant, member = member, stars = this.stars)
    }
}