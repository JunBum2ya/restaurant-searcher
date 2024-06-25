package com.midas.restaurant.member.domain.cache

import com.midas.restaurant.common.domain.cache.BaseCacheEntity
import java.time.LocalDateTime

class MemberCache(
    private val id: Long,
    private val username: String,
    private val password: String,
    private val email: String,
    private val createdAt: LocalDateTime? = null,
    private val updatedAt: LocalDateTime? = null
) : BaseCacheEntity {
    override fun callId(): String {
        return id.toString()
    }

    override fun toString(): String {
        return "MemberCache(id=$id, username='$username', password='$password', email='$email', createdAt=$createdAt, updatedAt=$updatedAt)"
    }

}