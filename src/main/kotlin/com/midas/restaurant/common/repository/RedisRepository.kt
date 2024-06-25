package com.midas.restaurant.common.repository

interface RedisRepository<T, ID> {
    fun save(entity: T): T
    fun findByIdOrNull(id: ID): T?
    fun findAll(): List<T>
    fun delete(entity: T)
    fun deleteById(id: ID)
    fun deleteAll()
}