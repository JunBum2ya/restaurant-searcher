package com.midas.restaurant.restaurant.dto

import java.time.LocalDateTime

class RestaurantDetailDto(
    val id: Long? = null,
    val name: String,
    val address: String,
    val roadAddressName: String,
    val majorCategory: String? = null,
    val minorCategory: String? = null,
    val phoneNumber: String,
    val websiteUrl: String,
    val latitude: Double,
    val longitude: Double,
    val likes: Double,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)