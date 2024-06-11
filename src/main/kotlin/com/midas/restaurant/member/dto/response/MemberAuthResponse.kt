package com.midas.restaurant.member.dto.response

import com.midas.restaurant.member.dto.AuthToken

data class MemberAuthResponse(val username: String, val token: String) {
    companion object {
        fun from(authToken: AuthToken): MemberAuthResponse {
            return MemberAuthResponse(username = authToken.username, token = authToken.token)
        }
    }
}