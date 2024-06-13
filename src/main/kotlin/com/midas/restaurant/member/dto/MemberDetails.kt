package com.midas.restaurant.member.dto

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.common.util.DateUtil
import com.midas.restaurant.exception.CustomException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

class MemberDetails(
    val id: Long,
    private val username: String,
    private val password: String,
    private val email: String,
    private val expiredDate: LocalDateTime,
    private val authorities: MutableCollection<out GrantedAuthority> = mutableListOf(),
    private val createdAt: LocalDateTime? = null,
    private val updatedAt: LocalDateTime? = null
) : UserDetails {

    companion object {
        fun from(memberDto: MemberDto, expiredTime: Long, authorities: MutableCollection<out GrantedAuthority>): MemberDetails {
            return MemberDetails(
                id = memberDto.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                username = memberDto.username,
                password = memberDto.password,
                email = memberDto.email,
                expiredDate = DateUtil.now().plusNanos(expiredTime *  1_000_000),
                authorities = authorities,
                createdAt = memberDto.createdAt,
                updatedAt = memberDto.updatedAt
            )
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.authorities
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isAccountNonExpired(): Boolean {
        return expiredDate.isBefore(DateUtil.now())
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return expiredDate.isBefore(DateUtil.now())
    }

    override fun isEnabled(): Boolean {
        return true
    }
}