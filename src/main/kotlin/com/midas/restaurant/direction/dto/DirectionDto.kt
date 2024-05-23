package com.midas.restaurant.direction.dto

import com.midas.restaurant.common.dto.BaseTimeDto
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
}