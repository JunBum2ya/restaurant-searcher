package com.midas.restaurant.direction.dto

import com.midas.restaurant.common.dto.BaseTimeDto
import com.midas.restaurant.direction.domain.Direction
import java.time.LocalDateTime

class DirectionDto(
    val id: Long? = null,
    val inputAddress: String,
    val inputLatitude: Double,
    val inputLongitude: Double,
    val targetName: String,
    val targetAddress: String,
    val targetLatitude: Double,
    val targetLongitude: Double,
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
                targetName = direction.targetName,
                targetAddress = direction.targetAddress,
                targetLatitude = direction.targetLatitude,
                targetLongitude = direction.targetLongitude,
                distance = direction.distance,
                createdAt = direction.getCreatedAt(),
                updatedAt = direction.getUpdatedAt()
            )
        }
    }

    fun toEntity(): Direction {
        return Direction(
            inputAddress = inputAddress,
            inputLatitude = inputLatitude,
            inputLongitude = inputLongitude,
            targetName = targetName,
            targetAddress = targetAddress,
            targetLatitude = targetLatitude,
            targetLongitude = targetLongitude,
            distance = distance
        )
    }

}