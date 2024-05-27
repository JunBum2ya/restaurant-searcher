package com.midas.restaurant.direction.dto

import com.midas.restaurant.common.dto.BaseTimeDto
import com.midas.restaurant.direction.domain.Direction
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.dto.RestaurantDto
import java.time.LocalDateTime

open class DirectionDto(
    val id: Long? = null,
    val inputAddress: String,
    val inputLatitude: Double,
    val inputLongitude: Double,
    val distance: Double,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null
) : BaseTimeDto(createdAt, updatedAt) {

    companion object {
        fun from(direction: Direction): DirectionDto {
            return DirectionDto(
                id = direction.getId(),
                inputAddress = direction.inputAddress,
                inputLatitude = direction.inputLatitude,
                inputLongitude = direction.inputLongitude,
                distance = direction.distance,
                createdAt = direction.getCreatedAt(),
                updatedAt = direction.getUpdatedAt()
            )
        }
    }

    fun toEntity(restaurant: Restaurant): Direction {
        return Direction(
            inputAddress = inputAddress,
            inputLatitude = inputLatitude,
            inputLongitude = inputLongitude,
            restaurant = restaurant,
            distance = distance
        )
    }

}