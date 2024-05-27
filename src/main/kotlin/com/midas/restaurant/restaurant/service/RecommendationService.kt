package com.midas.restaurant.restaurant.service

import com.midas.restaurant.api.service.KakaoApiService
import com.midas.restaurant.direction.service.DirectionService
import com.midas.restaurant.restaurant.dto.RestaurantDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RecommendationService(
    private val kakaoApiService: KakaoApiService,
    private val directionService: DirectionService
) {

    private val log = LoggerFactory.getLogger(RecommendationService::class.java)

    fun recommendRestaurantList(address: String): List<RestaurantDto> {
        val kakaoApiResponse = kakaoApiService.requestAddressSearch(address)

        if(kakaoApiResponse == null || kakaoApiResponse.documents.isEmpty()) {
            log.error("[PharmacyRecommendationService.recommendPharmacyList fail] Input address: {}", address)
            return emptyList()
        }

        val document = kakaoApiResponse.documents[0]

        val directionList = directionService.buildDirectionListByCategoryApi(document)
        return directionList.map { it.restaurant }
    }

}