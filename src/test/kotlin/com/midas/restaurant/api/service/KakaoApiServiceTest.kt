package com.midas.restaurant.api.service

import com.midas.restaurant.api.contant.KakaoCategory
import com.midas.restaurant.api.dto.response.KakaApiResponse
import com.midas.restaurant.api.dto.response.MetaResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.net.URI

class KakaoApiServiceTest : BehaviorSpec({
    val kakaoRestApiKey = "KAKAO_REST_API_KEY"
    val restTemplate = mockk<RestTemplate>()
    val kakaoApiService = KakaoApiService(kakaoRestApiKey, restTemplate)

    Given("위도와 경도 데이터, 범위와 카테고리가 주어졌을 때") {
        val latitude = 127.309360
        val longitude = 37.171820
        val radius = 20.0
        val response =
            KakaApiResponse(meta = MetaResponse(totalCount = 10, pageableCount = 10, end = true), documents = listOf())
        val category = KakaoCategory.RESTAURANT
        every { restTemplate.exchange<KakaApiResponse>(any(URI::class), HttpMethod.GET, any(HttpEntity::class)) }
            .returns(ResponseEntity.ok(response))
        When("카카오 APi에서 조회를 한다면") {
            val kakaoApiResponse = kakaoApiService.requestCategorySearch(latitude, longitude, radius, category)
            Then("조회데이터가 반환된다.") {
                kakaoApiResponse shouldNotBe null
                kakaoApiResponse?.documents?.isEmpty() shouldBe true
                verify { restTemplate.exchange<KakaApiResponse>(any(URI::class), HttpMethod.GET, any(HttpEntity::class)) }
            }
        }
    }
})