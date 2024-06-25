package com.midas.restaurant.member.controller

import com.midas.restaurant.common.dto.response.CommonResponse
import com.midas.restaurant.member.dto.request.MemberJoinRequest
import com.midas.restaurant.member.dto.request.MemberLoginRequest
import com.midas.restaurant.member.dto.response.MemberAuthResponse
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
    fun join(@Valid @RequestBody request: MemberJoinRequest): ResponseEntity<CommonResponse<MemberAuthResponse>> {
        val tokenData = memberService.join(request.toDto())
        return ResponseEntity.ok(CommonResponse.of(MemberAuthResponse.from(tokenData)))
    }

    @PostMapping("login")
    fun login(@Valid @RequestBody request: MemberLoginRequest): ResponseEntity<CommonResponse<MemberAuthResponse>> {
        val tokenData = memberService.login(username = request.username!!, password = request.password!!)
        return ResponseEntity.ok(CommonResponse.of(MemberAuthResponse.from(tokenData)))
    }

}