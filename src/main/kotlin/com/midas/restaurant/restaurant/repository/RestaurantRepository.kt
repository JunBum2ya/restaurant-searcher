package com.midas.restaurant.restaurant.repository;

import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.dto.RestaurantDetailDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    @Query("select new com.midas.restaurant.restaurant.dto.RestaurantDetailDto(c.id, c.name, c.address, c.roadAddressName, c.majorCategory, c.minorCategory, c.phoneNumber, c.websiteUrl, c.latitude, c.longitude, avg(m.stars) ,c.createdAt, c.updatedAt) from Restaurant c left join RestaurantLike m on c = m.restaurant where c.id = :id")
    fun findRestaurantWithLikeAverage(id: Long): RestaurantDetailDto
}