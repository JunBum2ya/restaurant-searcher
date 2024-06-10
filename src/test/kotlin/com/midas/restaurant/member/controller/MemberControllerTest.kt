package com.midas.restaurant.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.exception.CustomExceptionHandler
import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.member.dto.request.MemberJoinRequest
import com.midas.restaurant.member.service.MemberService
import io.kotest.assertions.print.print
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class MemberControllerTest : DescribeSpec({

    val memberService = mockk<MemberService>()
    val memberController = MemberController(memberService)
    val objectMapper = ObjectMapper()
    val mvc = MockMvcBuilders.standaloneSetup(memberController, CustomExceptionHandler()).build()

    describe("회원가입") {
        val username = "username"
        val password = "password"
        val email = "test@test.com"
        every { memberService.join(any(MemberDto::class)) }.returns(
            MemberDto(
                username = username,
                password = password,
                email = email
            )
        )

        it("200 OK") {
            mvc.perform(
                post("/api/v1/members/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(MemberJoinRequest(username, password, email)))
            ).andExpect(status().isOk)
            verify { memberService.join(any(MemberDto::class)) }
        }
    }
    describe("이미 등록된 username으로 회원가입할 경우") {
        val username = "username"
        val password = "password"
        every { memberService.join(any(MemberDto::class)) }.throws(
            CustomException(
                code = "001",
                message = "중복되었습니다.",
                status = ResultStatus.DUPLICATE_UNIQUE_PROPERTY
            )
        )
        it("404 오류가 발생한다.") {
            mvc.perform(
                post("/api/v1/members/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(MemberJoinRequest(username, password, username)))
            ).andExpect(status().isConflict)
            verify { memberService.join(any(MemberDto::class)) }
        }
    }
})