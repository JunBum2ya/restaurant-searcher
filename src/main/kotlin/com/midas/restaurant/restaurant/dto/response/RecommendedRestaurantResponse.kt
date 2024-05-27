package com.midas.restaurant.restaurant.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.midas.restaurant.restaurant.dto.RestaurantDto
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RecommendedRestaurantResponse(
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
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(restaurant: RestaurantDto): RecommendedRestaurantResponse {
            return RecommendedRestaurantResponse(
                id = restaurant.id,
                name = restaurant.name,
                address = restaurant.address,
                roadAddressName = restaurant.roadAddressName,
                majorCategory = restaurant.majorCategory,
                minorCategory = restaurant.minorCategory,
                phoneNumber = restaurant.phoneNumber,
                websiteUrl = restaurant.websiteUrl,
                latitude = restaurant.latitude,
                longitude = restaurant.longitude,
                createdAt = restaurant.createdAt,
                updatedAt = restaurant.updatedAt
            )
        }
    }
}