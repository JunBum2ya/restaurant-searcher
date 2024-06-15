package com.midas.restaurant.restaurant.dto.request

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.restaurant.dto.RestaurantDto
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class RestaurantRequest(
    @field:NotEmpty(message = "이름을 입력하세요.") val name: String?,
    @field:NotEmpty(message = "주소를 입력하세요.") val address: String?,
    @field:NotEmpty(message = "도로명 주소를 입력하세요.") val roadAddressName: String?,
    val majorCategory: String? = null,
    val minorCategory: String? = null,
    @field:NotEmpty(message = "전화번호를 입력하세요.") val phoneNumber: String?,
    @field:NotEmpty(message = "웹사이트 URL을 입력하세요.") val websiteUrl: String?,
    @field:NotNull(message = "경도를 입력하세요.") val latitude: Double?,
    @field:NotNull(message = "위도를 입력하세요.") val longitude: Double?
) {
    @Throws(CustomException::class)
    fun toDto(): RestaurantDto {
        return RestaurantDto(
            name = name ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            address = address ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            roadAddressName = roadAddressName ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            majorCategory = majorCategory,
            minorCategory = minorCategory,
            phoneNumber = phoneNumber ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            websiteUrl = websiteUrl ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            latitude = latitude ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            longitude = longitude ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST)
        )
    }
}
