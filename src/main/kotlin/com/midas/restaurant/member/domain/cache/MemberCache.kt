package com.midas.restaurant.member.domain.cache

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.common.domain.cache.BaseCacheEntity
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.domain.Member
import java.time.LocalDateTime

class MemberCache(
    val id: Long,
    val username: String,
    val password: String,
    val email: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) : BaseCacheEntity {
    override fun callId(): String {
        return username
    }

    override fun toString(): String {
        return "MemberCache(id=$id, username='$username', password='$password', email='$email', createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    companion object {
        fun from(member: Member): MemberCache {
            return MemberCache(
                id = member.getId() ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                username = member.getUsername(),
                password = member.getPassword(),
                email = member.getEmail()
            )
        }
    }

}