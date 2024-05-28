package com.midas.restaurant.restaurant.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.restaurant.domain.cache.RestaurantCache
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RestaurantRedisRepository(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val CACHE_KEY = "RESTAURANT"

    private val hashOperations: HashOperations<String, Long, String> = redisTemplate.opsForHash()

    fun save(restaurant: RestaurantCache): RestaurantCache {
        hashOperations.put(CACHE_KEY, restaurant.id, serialize(restaurant))
        return restaurant
    }

    fun findAll(): List<RestaurantCache> {
        return hashOperations.entries(CACHE_KEY).values.map { deserialize(it) }
    }

    fun delete(restaurant: RestaurantCache) {
        hashOperations.delete(CACHE_KEY, restaurant.id)
    }

    fun deleteById(id: Long) {
        hashOperations.delete(CACHE_KEY, id)
    }

    private fun serialize(restaurant: RestaurantCache): String {
        return objectMapper.writeValueAsString(restaurant)
    }

    private fun deserialize(restaurant: String): RestaurantCache {
        return objectMapper.readValue(restaurant, RestaurantCache::class.java)
    }

}