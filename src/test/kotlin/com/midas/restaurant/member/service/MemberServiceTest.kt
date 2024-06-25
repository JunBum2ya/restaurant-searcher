package com.midas.restaurant.member.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.common.component.JwtTokenProvider
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.domain.cache.MemberCache
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.member.repository.MemberRedisRepository
import com.midas.restaurant.member.repository.MemberRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.mockito.ArgumentMatchers.anyString
import org.springframework.security.crypto.password.PasswordEncoder

class MemberServiceTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerTest

    val memberRepository = mockk<MemberRepository>()
    val memberRedisRepository = mockk<MemberRedisRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val memberService = MemberService(memberRepository, memberRedisRepository, passwordEncoder, jwtTokenProvider)

    Given("레디스와 데이터베이스에 데이터가 없고 회원 데이터가 주어졌을 때") {
        val memberData = MemberDto(username = "username", password = "password", email = "test@test.com")
        every { memberRedisRepository.findByIdOrNull(any(String::class)) }.returns(null)
        every { memberRepository.findMemberByUsername(any(String::class)) }.returns(null)
        val testMember = Member(id = 1, username = "username", password = "password", email = "test@test.com")
        every { memberRepository.save(any(Member::class)) }.returns(testMember)
        every { memberRedisRepository.save(any(MemberCache::class)) }.returns(MemberCache.from(testMember))
        every { passwordEncoder.encode(any(String::class)) }.returns("encoded password")
        every { jwtTokenProvider.generateToken(any(MemberDto::class)) }.returns("token")
        When("회원가입을 진행하면") {
            val member = memberService.join(memberData)
            Then("저장 후 회원 데이터가 반환된다.") {
                member shouldNotBe null
                member.username shouldBe "username"
                member.token shouldBe "token"
                verify { memberRedisRepository.findByIdOrNull("username") }
                verify { memberRepository.findMemberByUsername(any(String::class)) }
                verify { memberRepository.save(any(Member::class)) }
                verify { memberRedisRepository.save(any(MemberCache::class)) }
                verify { passwordEncoder.encode(any(String::class)) }
                verify { jwtTokenProvider.generateToken(any(MemberDto::class)) }
            }
        }
    }

    Given("중복된 username을 가진 회원 데이터가 주어졌을 때") {
        val savedMember = Member(id = 1L, username = "username", password = "password", email = "test@test.com")
        val memberData = MemberDto(username = "username", password = "password", email = "test@test.com")
        every { memberRepository.findMemberByUsername(any(String::class)) }.returns(savedMember)
        every { memberRedisRepository.save(any(MemberCache::class)) }.returns(MemberCache.from(savedMember))
        When("레디스에 데이터가 저장되었을 때 회원가입을 진행하면") {
            every { memberRedisRepository.findByIdOrNull(any(String::class)) }.returns(MemberCache.from(savedMember))
            val exception = shouldThrowExactly<CustomException> { memberService.join(memberData) }
            Then("예외가 발생한다.") {
                exception shouldNotBe null
                exception.code shouldBe ResultStatus.DUPLICATE_UNIQUE_PROPERTY.code
                exception.message shouldBe "중복된 사용자가 있습니다."
            }
            Then("레디스에서 조회를 한다.") {
                verify { memberRedisRepository.findByIdOrNull(any(String::class)) }
            }
        }
        When("회원가입을 진행하면") {
            every { memberRedisRepository.findByIdOrNull(any(String::class)) }.returns(null)
            val exception = shouldThrowExactly<CustomException> { memberService.join(memberData) }
            Then("예외가 발생한다.") {
                exception shouldNotBe null
                exception.code shouldBe ResultStatus.DUPLICATE_UNIQUE_PROPERTY.code
                exception.message shouldBe "중복된 사용자가 있습니다."
                verify { memberRepository.findMemberByUsername(any(String::class)) }
            }
            Then("레디스에서 조회를 한다.") {
                verify { memberRedisRepository.findByIdOrNull("username") }
            }
            Then("레디스에 저장을 한다.") {
                verify { memberRedisRepository.save(any(MemberCache::class)) }
            }
        }
    }

    Given("username과 password가 주어졌을 때") {
        val username = "username"
        val password = "password"
        val member = Member(id = 1L, username = "username", password = "password", email = "test@test.com")
        every { memberRepository.findMemberByUsername(any(String::class)) }.returns(member)
        every { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }.returns(true)
        every { jwtTokenProvider.generateToken(any(MemberDto::class)) }.returns("token")
        every { memberRedisRepository.save(any(MemberCache::class)) }.returns(MemberCache.from(member))
        When("레디스에 데이터가 있을 때 로그인을 진행하면") {
            every { memberRedisRepository.findByIdOrNull(any(String::class)) }.returns(MemberCache.from(member))
            val authToken = memberService.login(username, password)
            Then("인증 데이터가 반환된다.") {
                authToken shouldNotBe null
                authToken.username shouldBe username
                authToken.token shouldBe "token"
                verify { memberRedisRepository.findByIdOrNull(any(String::class)) }
                verify { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }
                verify { jwtTokenProvider.generateToken(any(MemberDto::class)) }
            }
        }
        When("레디스에 데이터가 없을 때 로그인을 진행하면") {
            every { memberRedisRepository.findByIdOrNull(any(String::class)) }.returns(null)
            val authToken = memberService.login(username, password)
            Then("인증 데이터가 반환된다.") {
                authToken shouldNotBe null
                authToken.username shouldBe username
                authToken.token shouldBe "token"
                verify { memberRedisRepository.findByIdOrNull(any(String::class)) }
                verify { memberRepository.findMemberByUsername(any(String::class)) }
                verify { memberRedisRepository.save(any(MemberCache::class)) }
                verify { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }
                verify { jwtTokenProvider.generateToken(any(MemberDto::class)) }
            }
        }
    }

    Given("DB의 데이터와 일치하지 않는 password가 주어졌을 때") {
        val username = "username"
        val password = "password"
        val member = Member(id = 1L, username = "username", password = "wrong", email = "test@test.com")
        every { memberRedisRepository.findByIdOrNull(any(String::class)) }.returns(null)
        every { memberRedisRepository.save(any(MemberCache::class)) }.returns(MemberCache.from(member))
        every { memberRepository.findMemberByUsername(any(String::class)) }.returns(member)
        every { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }.returns(false)
        When("로그인을 진행하면") {
            val exception = shouldThrowExactly<CustomException> { memberService.login(username, password) }
            Then("예외가 발생한다.") {
                exception shouldNotBe null
                verify { memberRedisRepository.save(any(MemberCache::class)) }
                verify { memberRepository.findMemberByUsername(any(String::class)) }
                verify { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }
            }
        }
    }

    Given("존재하지 않는 username과 password가 주어졌을 때") {
        val username = "username"
        val password = "password"
        every { memberRedisRepository.findByIdOrNull(any(String::class)) }.returns(null)
        every { memberRepository.findMemberByUsername(any(String::class)) }.returns(null)
        When("로그인을 진행하면") {
            val exception = shouldThrowExactly<CustomException> { memberService.login(username, password) }
            Then("예외가 발생한다.") {
                exception shouldNotBe null
                verify { memberRedisRepository.findByIdOrNull(any(String::class)) }
                verify { memberRepository.findMemberByUsername(any(String::class)) }
            }
        }
    }
})