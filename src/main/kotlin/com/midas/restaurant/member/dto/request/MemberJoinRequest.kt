package com.midas.restaurant.member.dto.request

import com.midas.restaurant.member.dto.MemberDto
import jakarta.validation.constraints.NotEmpty

data class MemberJoinRequest(
    @field:NotEmpty(message = "아이디가 누락되었습니다.") var username: String?,
    @field:NotEmpty(message = "패스워드가 누락되었습니다.") var password: String?,
    @field:NotEmpty(message = "이메일이 누락되었습니다.") var email: String?
) {

    fun toDto(): MemberDto {
        return MemberDto(username = username!!, password = password!!, email = email!!)
    }

}