package com.midas.restaurant.direction.domain

import com.midas.restaurant.common.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Direction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private var id: Long? = null,
    val inputAddress: String,
    val inputLatitude: Double,
    val inputLongitude: Double,
    val targetName: String,
    val targetAddress: String,
    val targetLatitude: Double,
    val targetLongitude: Double,
    val distance: Double
): BaseEntity() {

    fun getId(): Long? {
        return id
    }

}