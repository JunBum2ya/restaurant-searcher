package com.midas.restaurant.restaurant.repository;

import com.midas.restaurant.restaurant.domain.Restaurant
import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantRepository : JpaRepository<Restaurant, Long> {
}