package com.midas.restaurant.member.dto.request

import jakarta.validation.constraints.NotEmpty

data class MemberLoginRequest(
    @field:NotEmpty(message = "유저 식별자를 입력하세요.") val username: String?,
    @field:NotEmpty(message = "패스워드를 입력하세요.") val password: String?
) {
}