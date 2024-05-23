package com.midas.restaurant.direction.repository;

import com.midas.restaurant.direction.domain.Direction
import org.springframework.data.jpa.repository.JpaRepository

interface DirectionRepository : JpaRepository<Direction, Long> {
}