package com.midas.restaurant.member.dto

import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.domain.cache.MemberCache
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

class MemberDto(
    val id: Long? = null,
    val username: String,
    val password: String,
    val email: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {

    companion object {
        fun from(member: Member): MemberDto {
            return MemberDto(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getUpdatedAt()
            )
        }

        fun from(member: MemberCache): MemberDto {
            return MemberDto(
                id = member.id,
                username = member.username,
                password = member.password,
                email = member.email,
                createdAt = member.createdAt,
                updatedAt = member.updatedAt
            )
        }
    }

    fun toEntity(): Member {
        return Member(username = username, password = password, email = email)
    }

    fun toEntity(passwordEncoder: PasswordEncoder): Member {
        return Member(username = username, password = passwordEncoder.encode(password), email = email)
    }

}