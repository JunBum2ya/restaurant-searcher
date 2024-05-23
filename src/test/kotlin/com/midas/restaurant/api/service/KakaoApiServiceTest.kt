package com.midas.restaurant.api.service

import com.midas.restaurant.api.contant.KakaoCategory
import com.midas.restaurant.api.dto.response.DocumentResponse
import com.midas.restaurant.api.dto.response.KakaoApiResponse
import com.midas.restaurant.api.dto.response.MetaResponse
import io.kotest.core.spec.style.BehaviorSpec
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

    Given("주소 데이터가 주어졌을 때") {
        val address = "경기도"
        val response = KakaoApiResponse(
            meta = MetaResponse(totalCount = 10, pageableCount = 10, end = true),
            documents = listOf(
                DocumentResponse(
                    id = "test",
                    placeName = "매운 갈비집",
                    addressName = "경기도",
                    roadAddressName = "경기도",
                    categoryGroupCode = "FD6",
                    categoryGroupName = "음식점",
                    categoryName = "음식점 > 양식",
                    distance = 4.35,
                    latitude = 4.345,
                    longitude = 4.345,
                    phone = "010-4534-4534",
                    placeUrl = "http://test.com",
                )
            )
        )
        every { restTemplate.exchange<KakaoApiResponse>(any(URI::class), HttpMethod.GET, any(HttpEntity::class)) }
            .returns(ResponseEntity.ok(response))
        When("카카오 API에서 조회를 한다면") {
            val kakaoApiResponse = kakaoApiService.requestAddressSearch(address)
            Then("조회데이터가 반환된다.") {
                kakaoApiResponse shouldNotBe null
                kakaoApiResponse?.documents?.isEmpty() shouldBe false
                verify { restTemplate.exchange<KakaoApiResponse>(any(URI::class), HttpMethod.GET, any(HttpEntity::class)) }
            }
        }

    }

    Given("위도와 경도 데이터, 범위와 카테고리가 주어졌을 때") {
        val latitude = 127.309360
        val longitude = 37.171820
        val radius = 20.0
        val response =
            KakaoApiResponse(meta = MetaResponse(totalCount = 10, pageableCount = 10, end = true), documents = listOf())
        val category = KakaoCategory.RESTAURANT
        every { restTemplate.exchange<KakaoApiResponse>(any(URI::class), HttpMethod.GET, any(HttpEntity::class)) }
            .returns(ResponseEntity.ok(response))
        When("카카오 APi에서 조회를 한다면") {
            val kakaoApiResponse = kakaoApiService.requestCategorySearch(latitude, longitude, radius, category)
            Then("조회데이터가 반환된다.") {
                kakaoApiResponse shouldNotBe null
                kakaoApiResponse?.documents?.isEmpty() shouldBe true
                verify {
                    restTemplate.exchange<KakaoApiResponse>(
                        any(URI::class),
                        HttpMethod.GET,
                        any(HttpEntity::class)
                    )
                }
            }
        }
    }
})