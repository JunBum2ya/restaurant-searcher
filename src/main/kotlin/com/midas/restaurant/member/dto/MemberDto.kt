package com.midas.restaurant.member.dto

import com.midas.restaurant.member.domain.Member
import java.time.LocalDateTime

class MemberDto(
    val id: Long? = null,
    val username: String,
    val password: String,
    val email: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {

    fun toEntity(): Member {
        return Member(username = username, password = password, email = email)
    }

}