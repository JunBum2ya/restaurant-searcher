package com.midas.restaurant.restaurant.service

import com.midas.restaurant.api.contant.KakaoCategory
import com.midas.restaurant.api.service.KakaoApiService
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val kakaoApiService: KakaoApiService
) {

    private val radius = 10.0
    private val category = KakaoCategory.RESTAURANT

    private val log = LoggerFactory.getLogger(RestaurantService::class.java)

    @Transactional(readOnly = true)
    fun searchRestaurantDtoList(): List<RestaurantDto> {
        return restaurantRepository.findAll().map { RestaurantDto.from(it) }
    }

    fun recommendRestaurantList(address: String) {

    }

    fun recommendRestaurantList(latitude: Double, longitude: Double) {
        val kakaoApiResponse = kakaoApiService.requestCategorySearch(latitude, longitude, radius, category)

    }

}