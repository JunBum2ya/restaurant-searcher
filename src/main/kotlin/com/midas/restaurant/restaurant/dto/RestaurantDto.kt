package com.midas.restaurant.restaurant.dto

import com.midas.restaurant.common.dto.BaseTimeDto
import com.midas.restaurant.restaurant.domain.Restaurant
import java.time.LocalDateTime

class RestaurantDto(
    val id: Long?,
    val name: String,
    val address: String,
    val roadAddressName: String,
    val majorCategory: String? = null,
    val minorCategory: String? = null,
    val phoneNumber: String,
    val websiteUrl: String,
    val latitude: Double,
    val longitude: Double,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null
) : BaseTimeDto(createdAt, updatedAt) {

    companion object {
        fun from(restaurant: Restaurant): RestaurantDto {
            return RestaurantDto(
                id = restaurant.getId(),
                name = restaurant.name,
                address = restaurant.address,
                roadAddressName = restaurant.roadAddressName,
                majorCategory = restaurant.majorCategory,
                minorCategory = restaurant.minorCategory,
                phoneNumber = restaurant.phoneNumber,
                websiteUrl = restaurant.websiteUrl,
                latitude = restaurant.latitude,
                longitude = restaurant.longitude,
                createdAt = restaurant.getCreatedAt(),
                updatedAt = restaurant.getUpdatedAt()
            )
        }
    }

    fun toEntity(): Restaurant {
        return Restaurant(
            name = name,
            address = address,
            roadAddressName = roadAddressName,
            majorCategory = majorCategory,
            minorCategory = minorCategory,
            phoneNumber = phoneNumber,
            websiteUrl = websiteUrl,
            latitude = latitude,
            longitude = longitude
        )
    }

}
