package com.midas.restaurant.member.dto.request

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import jakarta.validation.constraints.NotEmpty

data class MemberLoginRequest(
    @field:NotEmpty(message = "유저 식별자를 입력하세요.") val username: String?,
    @field:NotEmpty(message = "패스워드를 입력하세요.") val password: String?
)