package com.midas.restaurant.member.dto

import com.midas.restaurant.member.domain.Member
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

    constructor(member: Member) : this(
        member.getId(),
        member.getUsername(),
        member.getPassword(),
        member.getEmail(),
        member.getCreatedAt(),
        member.getUpdatedAt()
    )

    fun toEntity(): Member {
        return Member(username = username, password = password, email = email)
    }

    fun toEntity(passwordEncoder: PasswordEncoder): Member {
        return Member(username = username, password = passwordEncoder.encode(password), email = email)
    }

}