package com.midas.restaurant.restaurant.dto.response

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.restaurant.dto.RestaurantDetailDto
import java.time.LocalDateTime
import kotlin.jvm.Throws

data class RestaurantDetailResponse(
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
    val likes: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {

    companion object {
        @Throws(CustomException::class)
        fun from(restaurant: RestaurantDetailDto): RestaurantDetailResponse {
            return RestaurantDetailResponse(
                id = restaurant.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                name = restaurant.name,
                address = restaurant.address,
                roadAddressName = restaurant.roadAddressName,
                majorCategory = restaurant.majorCategory,
                minorCategory = restaurant.minorCategory,
                phoneNumber = restaurant.phoneNumber,
                websiteUrl = restaurant.websiteUrl,
                latitude = restaurant.latitude,
                longitude = restaurant.longitude,
                likes = restaurant.likes,
                createdAt = restaurant.createdAt ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                updatedAt = restaurant.updatedAt ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY)
            )
        }
    }

}