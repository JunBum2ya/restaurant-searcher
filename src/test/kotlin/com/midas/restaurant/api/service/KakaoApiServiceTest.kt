package com.midas.restaurant.api.service

import com.midas.restaurant.api.contant.KakaoCategory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.web.client.RestTemplate

class KakaoApiServiceTest: BehaviorSpec({
    val kakaoRestApiKey = "KAKAO_REST_API_KEY"
    val restTemplate = RestTemplate()
    val kakaoApiService = KakaoApiService(kakaoRestApiKey,restTemplate)

    Given("위도와 경도 데이터, 범위와 카테고리가 주어졌을 때") {
        val latitude = 127.309360
        val longitude = 37.171820
        val radius = 20.0
        val category = KakaoCategory.RESTAURANT
        When("카카오 APi에서 조회를 한다면") {
            val kakaoApiResponse = kakaoApiService.requestCategorySearch(latitude, longitude, radius, category)
            Then("조회데이터가 반환된다.") {
                kakaoApiResponse shouldNotBe null
            }
        }
    }
})