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
class RestaurantService(private val restaurantRepository: RestaurantRepository) {

    @Transactional(readOnly = true)
    fun searchRestaurantDtoList(): List<RestaurantDto> {
        return restaurantRepository.findAll().map { RestaurantDto.from(it) }
    }

}