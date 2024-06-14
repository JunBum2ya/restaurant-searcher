package com.midas.restaurant.member.dto

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.common.util.DateUtil
import com.midas.restaurant.exception.CustomException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class MemberDetails(
    val id: Long,
    private val username: String,
    private val password: String,
    val email: String,
    val expiredDate: LocalDateTime,
    private val authorities: MutableCollection<out GrantedAuthority> = mutableListOf(),
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) : UserDetails {

    companion object {
        fun from(
            memberDto: MemberDto,
            expiredTime: Long,
            authorities: MutableCollection<out GrantedAuthority>
        ): MemberDetails {
            return MemberDetails(
                id = memberDto.id ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                username = memberDto.username,
                password = memberDto.password,
                email = memberDto.email,
                expiredDate = DateUtil.now().plusNanos(expiredTime * 1_000_000),
                authorities = authorities,
                createdAt = memberDto.createdAt,
                updatedAt = memberDto.updatedAt
            )
        }

        fun deserialize(data: String): MemberDetails {
            val localDateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val array = data.split("|")
            return MemberDetails(
                id = array[0].toLong(),
                username = array[1],
                password = array[2],
                email = array[3],
                expiredDate = LocalDateTime.parse(array[4], localDateTimePattern),
                authorities = array[5].split(",").stream().map { SimpleGrantedAuthority(it) }
                    .collect(Collectors.toList()),
                createdAt = LocalDateTime.parse(array[6], localDateTimePattern),
                updatedAt = LocalDateTime.parse(array[7], localDateTimePattern)
            )
        }
    }

    fun serialize(): String {
        val localDateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val array = Array(8) { "" }
        array[0] = id.toString()
        array[1] = username
        array[2] = password
        array[3] = email
        array[4] = expiredDate.format(localDateTimePattern)
        array[5] = authorities.map { it.authority }.joinToString(",")
        array[6] = createdAt?.format(localDateTimePattern) ?: DateUtil.now().format(localDateTimePattern)
        array[7] = updatedAt?.format(localDateTimePattern) ?: DateUtil.now().format(localDateTimePattern)
        val test = array.joinToString("|")
        return test
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