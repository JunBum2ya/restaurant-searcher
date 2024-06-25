package com.midas.restaurant.common.domain.cache

interface BaseCacheEntity {
    fun callId(): String
    override fun toString(): String
}