package com.midas.restaurant.restaurant.service

import com.midas.restaurant.api.contant.KakaoCategory
import com.midas.restaurant.api.service.KakaoApiService
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.domain.cache.RestaurantCache
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.restaurant.repository.RestaurantRedisRepository
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val restaurantRedisRepository: RestaurantRedisRepository
) {

    @Transactional(readOnly = true)
    fun searchRestaurantDtoList(): List<RestaurantDto> {
        val restaurantCacheList = restaurantRedisRepository.findAll()
        return if (restaurantCacheList.isEmpty()) {
            restaurantRepository.findAll()
                .map {
                    RestaurantCache(
                        id = it.getId()!!,
                        name = it.name,
                        address = it.address,
                        roadAddressName = it.roadAddressName,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        phoneNumber = it.phoneNumber,
                        websiteUrl = it.websiteUrl,
                        createdAt = it.getCreatedAt(),
                        updatedAt = it.getUpdatedAt()
                    )
                }
                .map { restaurantRedisRepository.save(it) }
                .map { RestaurantDto.from(it) }
        } else {
            restaurantCacheList.map { RestaurantDto.from(it) }
        }
    }

    @Transactional
    fun saveRestaurantList(restaurantDtoList: List<RestaurantDto>): List<RestaurantDto> {
        val restaurantList = restaurantDtoList.map { it.toEntity() }
        return restaurantRepository.saveAll(restaurantList).map { RestaurantDto.from(it) }
    }

    @Transactional
    fun saveRestaurant(restaurantDto: RestaurantDto): RestaurantDto {
        val restaurant = restaurantRepository.save(restaurantDto.toEntity())
        return RestaurantDto.from(restaurant)
    }

}