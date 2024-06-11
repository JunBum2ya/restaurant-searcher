package com.midas.restaurant.member.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.member.repository.MemberRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.mockito.ArgumentMatchers.anyString
import org.springframework.security.crypto.password.PasswordEncoder

class MemberServiceTest : BehaviorSpec({

    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberService = MemberService(memberRepository,passwordEncoder)

    Given("회원 데이터가 주어졌을 때") {
        val memberData = MemberDto(username = "username", password = "password", email = "test@test.com")
        every { memberRepository.findMemberByUsername(any(String::class)) }.returns(null)
        every { memberRepository.save(any(Member::class)) }.returns(Member(username = "username", password = "password", email = "test@test.com"))
        every { passwordEncoder.encode(any(String::class)) }.returns("encoded password")
        When("회원가입을 진행하면") {
            val member = memberService.join(memberData)
            Then("저장 후 회원 데이터가 반환된다.") {
                member shouldNotBe null
                member.username shouldBe "username"
                member.password shouldBe "password"
                verify { memberRepository.findMemberByUsername(any(String::class)) }
                verify { memberRepository.save(any(Member::class)) }
                verify { passwordEncoder.encode(any(String::class)) }
            }
        }
    }

    Given("중복된 username을 가진 회원 데이터가 주어졌을 때") {
        val memberData = MemberDto(username = "username", password = "password", email = "test@test.com")
        every { memberRepository.findMemberByUsername(any(String::class)) }
            .returns(Member(username = "username", password = "password", email = "test@test.com"))
        When("회원가입을 진행하면") {
            val exception = shouldThrowExactly<CustomException> { memberService.join(memberData) }
            Then("예외가 발생한다.") {
                exception shouldNotBe null
                exception.code shouldBe ResultStatus.DUPLICATE_UNIQUE_PROPERTY.code
                exception.message shouldBe "중복된 사용자가 있습니다."
                verify { memberRepository.findMemberByUsername(any(String::class)) }
            }
        }
    }

    Given("username과 password가 주어졌을 때") {
        val username = "username"
        val password = "password"
        every { memberRepository.findMemberByUsername(any(String::class)) }
            .returns(Member(username = "username", password = "password", email = "test@test.com"))
        every { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }.returns(true)
        When("로그인을 진행하면") {
            val authToken = memberService.login(username, password)
            Then("인증 데이터가 반환된다.") {
                authToken shouldNotBe null
                authToken.username shouldBe username
                authToken.token shouldBe "token"
                verify { memberRepository.findMemberByUsername(any(String::class)) }
                verify { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }
            }
        }
    }

    Given("username과 일치하지 않는 password가 주어졌을 때") {
        val username = "username"
        val password = "password"
        every { memberRepository.findMemberByUsername(any(String::class)) }
            .returns(Member(username = "username", password = "wrong", email = "test@test.com"))
        every { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }.returns(false)
        When("로그인을 진행하면") {
            val exception = shouldThrowExactly<CustomException> { memberService.login(username, password) }
            Then("예외가 발생한다.") {
                exception shouldNotBe null
                verify { memberRepository.findMemberByUsername(any(String::class)) }
                verify { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }
            }
        }
    }

    Given("존재하지 않는 username과 password가 주어졌을 때") {
        val username = "username"
        val password = "password"
        every { memberRepository.findMemberByUsername(any(String::class)) }.returns(null)
        When("로그인을 진행하면") {
            val exception = shouldThrowExactly<CustomException> { memberService.login(username, password) }
            Then("예외가 발생한다.") {
                exception shouldNotBe null
                verify { memberRepository.findMemberByUsername(any(String::class)) }
            }
        }
    }
})