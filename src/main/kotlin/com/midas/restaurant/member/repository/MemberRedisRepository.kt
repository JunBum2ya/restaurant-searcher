package com.midas.restaurant.member.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.common.repository.RedisRepository
import com.midas.restaurant.member.domain.cache.MemberCache
import com.midas.restaurant.restaurant.domain.cache.RestaurantCache
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class MemberRedisRepository(
    redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) : RedisRepository<MemberCache, Long> {

    private val log = LoggerFactory.getLogger(MemberRedisRepository::class.java)

    private val CACHE_KEY = "MEMBER"

    private val hashOperations: HashOperations<String, String, String> = redisTemplate.opsForHash()

    private fun serialize(member: MemberCache): String {
        return objectMapper.writeValueAsString(member)
    }

    private fun deserialize(member: String): MemberCache {
        return objectMapper.readValue(member, MemberCache::class.java)
    }

    override fun save(entity: MemberCache): MemberCache {
        log.debug("Saving entity: {}", entity)
        hashOperations.put(CACHE_KEY, entity.callId(), serialize(entity))
        return entity
    }

    override fun findByIdOrNull(id: Long): MemberCache? {
        log.debug("Found entity: {}", id)
        val entity = hashOperations.get(CACHE_KEY, id.toString())
        return entity?.let { deserialize(it) }
    }

    override fun findAll(): List<MemberCache> {
        log.debug("Finding all records")
        return hashOperations.entries(CACHE_KEY).values.map { deserialize(it) }
    }

    override fun deleteAll() {
        log.debug("Deleting all records")
        hashOperations.entries(CACHE_KEY).values.map { deserialize(it) }
            .forEach { hashOperations.delete(CACHE_KEY, it.callId()) }
    }

    override fun deleteById(id: Long) {
        log.debug("Deleting id: {}", id)
        hashOperations.delete(CACHE_KEY, id.toString())
    }

    override fun delete(entity: MemberCache) {
        log.debug("Deleting record: {}", entity)
        hashOperations.delete(CACHE_KEY, entity.callId())
    }

}