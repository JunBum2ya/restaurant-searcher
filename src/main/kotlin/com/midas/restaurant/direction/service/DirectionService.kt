package com.midas.restaurant.direction.service

import com.midas.restaurant.api.contant.KakaoCategory
import com.midas.restaurant.api.dto.response.DocumentResponse
import com.midas.restaurant.api.service.KakaoApiService
import com.midas.restaurant.direction.dto.DirectionDto
import com.midas.restaurant.restaurant.service.RestaurantService
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class DirectionService(private val restaurantService: RestaurantService, private val kakaoApiService: KakaoApiService) {
    private val MAX_SEARCH_COUNT = 3L // 최대 검색 갯수
    private val RADIUS_KM = 10.0 // 반경 10 km
    private val DIRECTION_BASE_URL = "https://map.kakao.com/link/map/"

    fun buildDirectionList(documentResponse: DocumentResponse): List<DirectionDto> {
        return restaurantService.searchRestaurantDtoList()
            .stream()
            .map {
                DirectionDto(
                    inputAddress = documentResponse.addressName,
                    inputLatitude = documentResponse.latitude,
                    inputLongitude = documentResponse.longitude,
                    targetName = it.name,
                    targetAddress = it.address,
                    targetLatitude = it.latitude,
                    targetLongitude = it.longitude,
                    distance = calculateDistance(
                        documentResponse.latitude,
                        documentResponse.longitude,
                        it.latitude,
                        it.longitude
                    )
                )
            }.filter { it.distance <= RADIUS_KM }
            .sorted(Comparator.comparing { it.distance })
            .limit(MAX_SEARCH_COUNT)
            .collect(Collectors.toList())
    }

    fun buildDirectionListByCategoryApi(documentResponse: DocumentResponse): List<DirectionDto> {
        val documentList = kakaoApiService.requestCategorySearch(
            documentResponse.latitude,
            documentResponse.longitude,
            RADIUS_KM,
            KakaoCategory.RESTAURANT
        )?.documents

        if (documentList.isNullOrEmpty()) {
            return emptyList()
        }

        return documentList.stream()
            .map {
                DirectionDto(
                    inputAddress = documentResponse.addressName,
                    inputLatitude = documentResponse.latitude,
                    inputLongitude = documentResponse.longitude,
                    targetName = it.placeName,
                    targetAddress = it.addressName,
                    targetLatitude = it.latitude,
                    targetLongitude = it.longitude,
                    distance = calculateDistance(
                        documentResponse.latitude,
                        documentResponse.longitude,
                        it.latitude,
                        it.longitude
                    )
                )
            }.limit(MAX_SEARCH_COUNT)
            .collect(Collectors.toList())
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val lat1 = Math.toRadians(lat1)
        val lon1 = Math.toRadians(lon1)
        val lat2 = Math.toRadians(lat2)
        val lon2 = Math.toRadians(lon2)

        val earthRadius = 6371; //Kilometers

        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

    }

}