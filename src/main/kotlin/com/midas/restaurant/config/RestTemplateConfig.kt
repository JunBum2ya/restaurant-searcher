package com.midas.restaurant.config

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.DefaultStringRedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun initializeRedis(redisTemplate: RedisTemplate<String, Any>, connectionFactory: RedisConnectionFactory): CommandLineRunner {
        val redisConnection = connectionFactory.connection
        val redisSerializer = redisTemplate.keySerializer as RedisSerializer<String>
        return CommandLineRunner {
            val defaultRedisConnection = DefaultStringRedisConnection(redisConnection,redisSerializer)
            defaultRedisConnection.flushAll()
        }
    }

}