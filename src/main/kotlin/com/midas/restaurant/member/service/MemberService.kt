package com.midas.restaurant.member.service

import com.midas.restaurant.member.dto.MemberDto
import org.springframework.stereotype.Service

@Service
class MemberService {

    fun join(memberDto: MemberDto): MemberDto {
        return memberDto
    }

}