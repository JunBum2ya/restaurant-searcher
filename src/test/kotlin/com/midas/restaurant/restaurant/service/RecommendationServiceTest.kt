package com.midas.restaurant.restaurant.service

import com.midas.restaurant.api.dto.response.AddressDocumentResponse
import com.midas.restaurant.api.dto.response.CategoryDocumentResponse
import com.midas.restaurant.api.dto.response.KakaoApiResponse
import com.midas.restaurant.api.dto.response.MetaResponse
import com.midas.restaurant.api.service.KakaoApiService
import com.midas.restaurant.direction.dto.DirectionWithRestaurantDto
import com.midas.restaurant.direction.service.DirectionService
import com.midas.restaurant.restaurant.dto.RestaurantDto
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class RecommendationServiceTest : BehaviorSpec({

    val kakaoApiService = mockk<KakaoApiService>()
    val directionService = mockk<DirectionService>()
    val recommendationService = RecommendationService(kakaoApiService, directionService)

    Given("주소가 주어졌을 때") {
        val address = "경기도"
        val ownerId = 1L
        val document = CategoryDocumentResponse(
            id = "test",
            placeName = "장강",
            addressName = "경기도",
            roadAddressName = "경기도",
            phone = "test",
            placeUrl = "test",
            categoryGroupName = "test",
            categoryGroupCode = "test",
            categoryName = "test",
            distance = 124.0,
            latitude = 0.0,
            longitude = 0.0,
        )
        every { kakaoApiService.requestAddressSearch(any(String::class)) }.returns(
            KakaoApiResponse<AddressDocumentResponse>(
                meta = MetaResponse(
                    totalCount = 1,
                    pageableCount = 10,
                    end = true
                ),
                documents = listOf(AddressDocumentResponse(name = "test", latitude = 4.5, longitude = 2.5)),
            )
        )
        every { directionService.buildDirectionList(any(AddressDocumentResponse::class)) }.returns(
            listOf(
                DirectionWithRestaurantDto(
                    id = 1L,
                    inputLatitude = 123.4,
                    inputLongitude = 123.4,
                    inputAddress = "경기도",
                    distance = 0.0,
                    restaurant = RestaurantDto.from(document)
                ),
            )
        )
        When("주소를 기반으로 음식점을 추천하면") {
            val restaurantList = recommendationService.recommendRestaurantList(address, ownerId)
            Then("음식점 목록이 반환된다.") {
                restaurantList.size shouldBe 1
                verify { kakaoApiService.requestAddressSearch(any(String::class)) }
                verify { directionService.buildDirectionList(any(AddressDocumentResponse::class)) }
            }
        }
    }
    Given("주소가 주어졌지만 근처의 음식점이 없는 경우") {
        val ownerId = 1L
        val address = "경기도"
        every { kakaoApiService.requestAddressSearch(any(String::class)) }.returns(
            KakaoApiResponse(
                meta = MetaResponse(
                    totalCount = 10,
                    pageableCount = 10,
                    end = false
                ), documents = listOf()
            )
        )
        every { directionService.buildDirectionList(any(AddressDocumentResponse::class)) }.returns(listOf())
        When("주소를 기반으로 음식점을 조회를 하면") {
            val restaurantList = recommendationService.recommendRestaurantList(address, ownerId)
            Then("빈 리스트가 반환된다.") {
                restaurantList.isEmpty() shouldBe true
                verify { kakaoApiService.requestAddressSearch(any(String::class)) }
            }
        }
    }

}) {
}