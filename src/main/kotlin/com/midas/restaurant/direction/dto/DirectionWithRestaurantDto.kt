package com.midas.restaurant.direction.dto

import com.midas.restaurant.direction.domain.Direction
import com.midas.restaurant.restaurant.dto.RestaurantDto
import java.time.LocalDateTime

class DirectionWithRestaurantDto(
    id: Long? = null,
    inputAddress: String,
    inputLatitude: Double,
    inputLongitude: Double,
    distance: Double,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
    val restaurant: RestaurantDto
) : DirectionDto(
    id = id,
    inputAddress = inputAddress,
    inputLatitude = inputLatitude,
    inputLongitude = inputLongitude,
    distance = distance,
    createdAt = createdAt,
    updatedAt = updatedAt
) {

    companion object {
        fun from(direction: Direction): DirectionWithRestaurantDto {
            return DirectionWithRestaurantDto(
                id = direction.getId(),
                inputAddress = direction.inputAddress,
                inputLatitude = direction.inputLatitude,
                inputLongitude = direction.inputLongitude,
                distance = direction.distance,
                restaurant = RestaurantDto.from(direction.restaurant),
                createdAt = direction.getCreatedAt(),
                updatedAt = direction.getUpdatedAt()
            )
        }
    }

}