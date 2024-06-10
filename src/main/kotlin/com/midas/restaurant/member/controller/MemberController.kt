package com.midas.restaurant.member.controller

import com.midas.restaurant.member.dto.request.MemberJoinRequest
import com.midas.restaurant.member.dto.response.MemberResponse
import com.midas.restaurant.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
class MemberController(private val memberService: MemberService) {

    @PostMapping("join")
    fun join(@Valid @RequestBody request: MemberJoinRequest): ResponseEntity<MemberResponse> {
        val member = memberService.join(request.toDto())
        return ResponseEntity.ok(MemberResponse.from(member))
    }

}