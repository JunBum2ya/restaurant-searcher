package com.midas.restaurant.restaurant.repository;

import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.domain.RestaurantLike
import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantLikeRepository : JpaRepository<RestaurantLike, Long> {
    fun findRestaurantLikeByRestaurantAndMember(restaurant: Restaurant, member: Member): RestaurantLike?
}