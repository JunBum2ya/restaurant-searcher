package com.midas.restaurant.member.dto.response

import com.midas.restaurant.member.dto.MemberDto
import java.time.LocalDateTime

data class MemberResponse(
    val username: String,
    val email: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(member: MemberDto): MemberResponse {
            return MemberResponse(
                username = member.username,
                email = member.email,
                createdAt = member.createdAt,
                updatedAt = member.updatedAt
            )
        }
    }
}
