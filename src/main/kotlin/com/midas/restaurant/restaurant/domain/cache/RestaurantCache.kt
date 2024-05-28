package com.midas.restaurant.restaurant.domain.cache

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

class RestaurantCache(
    val id: Long,
    val name: String,
    val address: String,
    val roadAddressName: String,
    val majorCategory: String? = null,
    val minorCategory: String? = null,
    val phoneNumber: String,
    val websiteUrl: String,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
}