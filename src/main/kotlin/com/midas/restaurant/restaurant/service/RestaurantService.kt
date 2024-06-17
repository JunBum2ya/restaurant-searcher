package com.midas.restaurant.restaurant.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.cache.RestaurantCache
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.restaurant.dto.RestaurantLikeDto
import com.midas.restaurant.restaurant.repository.RestaurantLikeRepository
import com.midas.restaurant.restaurant.repository.RestaurantRedisRepository
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.View
import kotlin.jvm.Throws

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val restaurantRedisRepository: RestaurantRedisRepository,
    private val restaurantLikeRepository: RestaurantLikeRepository,
    private val memberRepository: MemberRepository
) {

    private val log = LoggerFactory.getLogger(RestaurantService::class.java)

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

    @Throws(CustomException::class)
    @Transactional
    fun saveRestaurantList(restaurantDtoList: List<RestaurantDto>, ownerId: Long): List<RestaurantDto> {
        try {
            val owner = memberRepository.getReferenceById(ownerId)
            val restaurantList = restaurantDtoList.map { it.toEntity(owner) }
            return restaurantRepository.saveAll(restaurantList).map { RestaurantDto.from(it) }
        } catch (e: EntityNotFoundException) {
            log.error(e.message)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Throws(CustomException::class)
    @Transactional
    fun saveRestaurant(restaurantDto: RestaurantDto, ownerId: Long): RestaurantDto {
        try {
            val owner = memberRepository.getReferenceById(ownerId)
            val restaurant = restaurantRepository.save(restaurantDto.toEntity(owner))
            return RestaurantDto.from(restaurant)
        } catch (e: EntityNotFoundException) {
            log.error(e.message)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Transactional
    fun likeRestaurant(restaurantId: Long, memberId: Long, restaurantLikeDto: RestaurantLikeDto): RestaurantLikeDto {
        try {
            val restaurant = restaurantRepository.getReferenceById(restaurantId)
            val member = memberRepository.getReferenceById(memberId)
            val restaurantLike = restaurantLikeRepository.findRestaurantLikeByRestaurantAndMember(restaurant, member)
                ?.update(stars = restaurantLikeDto.stars)
                ?: restaurantLikeRepository.save(restaurantLikeDto.toEntity(restaurant = restaurant, member = member))
            return RestaurantLikeDto.from(restaurantLike)
        } catch (e: EntityNotFoundException) {
            log.error(e.message)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Transactional
    fun cancelLikeRestaurant(restaurantId: Long, memberId: Long) {
        try {
            val restaurant = restaurantRepository.getReferenceById(restaurantId)
            val member = memberRepository.getReferenceById(memberId)
            val restaurantLike = restaurantLikeRepository.findRestaurantLikeByRestaurantAndMember(restaurant, member)
                ?:throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
            restaurantLikeRepository.delete(restaurantLike)
        } catch (e: EntityNotFoundException) {
            log.error(e.message)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

}