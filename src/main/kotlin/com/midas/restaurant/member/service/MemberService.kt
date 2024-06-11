package com.midas.restaurant.member.service

import com.midas.restaurant.member.dto.AuthToken
import com.midas.restaurant.member.dto.MemberDto
import org.springframework.stereotype.Service

@Service
class MemberService {

    fun join(memberDto: MemberDto): MemberDto {
        return memberDto
    }

    fun login(username: String, password: String): AuthToken {
        val authToken = AuthToken(username = username, token = "token")
        return authToken
    }

}