package com.midas.restaurant.common.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.member.dto.MemberDto
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.LocalDateTime

class TokenProviderTest : BehaviorSpec({

    val secret = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey"
    val tokenValidityInSeconds = 60L
    val objectMapper = ObjectMapper()
    val tokenProvider = JwtTokenProvider(secret, tokenValidityInSeconds, objectMapper)
    tokenProvider.afterPropertiesSet()

    Given("member details가 주어졌을 때") {
        val member = MemberDto(
            id = 1L,
            username = "testUser",
            password = "testPassword",
            email = "test@test.com"
        )
        When("토큰을 생성하면") {
            val token = tokenProvider.generateToken(member)
            Then("문자열 토큰이 반환된다.") {
                token shouldNotBe null
            }
        }
    }

    Given("Jwt 토큰이 주어졌을 때") {
        val token = tokenProvider.generateToken(
            MemberDto(
                id = 1L,
                username = "testUser",
                password = "testPassword",
                email = "test@test.com"
            )
        )
        When("토큰에서 인증정보를 추출하면") {
            val authentication = tokenProvider.extractAuthentication(token)
            Then("Authentication이 반환된다.") {
                authentication shouldNotBe null
                val details = authentication.principal as MemberDetails
                details.username shouldBe "testUser"
                details.password shouldBe "testPassword"
                details.email shouldBe "test@test.com"
            }
        }
    }

})