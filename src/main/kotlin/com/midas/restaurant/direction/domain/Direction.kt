package com.midas.restaurant.direction.domain

import com.midas.restaurant.common.domain.BaseEntity
import com.midas.restaurant.restaurant.domain.Restaurant
import jakarta.persistence.*

@Entity
class Direction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private var id: Long? = null,
    val inputAddress: String,
    val inputLatitude: Double,
    val inputLongitude: Double,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "restaurant_id") val restaurant: Restaurant,
    val distance: Double
) : BaseEntity() {

    fun getId(): Long? {
        return id
    }

}