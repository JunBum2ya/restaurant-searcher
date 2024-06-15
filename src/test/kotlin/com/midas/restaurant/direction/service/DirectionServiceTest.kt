package com.midas.restaurant.direction.service

import com.midas.restaurant.api.dto.response.AddressDocumentResponse
import com.midas.restaurant.api.dto.response.CategoryDocumentResponse
import com.midas.restaurant.api.service.KakaoApiService
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.member.service.MemberService
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.restaurant.service.RestaurantService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class DirectionServiceTest : BehaviorSpec({
    val restaurantService = mockk<RestaurantService>()
    val kakaoApiService = mockk<KakaoApiService>()
    val memberRepository = mockk<MemberRepository>()
    val directionService = DirectionService(restaurantService, memberRepository, kakaoApiService)

    val inputLatitude = 37.5960650456809
    val inputLongitude = 127.037033003036

    val document = AddressDocumentResponse(
        name = "만다린",
        latitude = inputLatitude,
        longitude = inputLongitude
    )

    val createRestaurantDto: (Long, String, Double, Double) -> RestaurantDto =
        { id, name, latitude, longitude ->
            RestaurantDto(
                id = id,
                name = name,
                address = "경기도",
                latitude = latitude,
                longitude = longitude,
                phoneNumber = "test",
                websiteUrl = "test",
                roadAddressName = "경기도"
            )
        }

    Given("DocumentDto가 주어졌을 때") {
        val restaurant1 = createRestaurantDto(1L, "중국요리", 37.61040424, 127.0569046)
        val restaurant2 = createRestaurantDto(2L, "칼국수", 37.60894036, 127.029052)
        every { restaurantService.searchRestaurantDtoList() }.returns(listOf(restaurant1, restaurant2))
        When("경로를 조회를 한다면") {
            val directions = directionService.buildDirectionList(document)
            Then("결과 값이 거리순으로 조회가 된다.") {
                directions.size shouldBe 2
                directions[0].restaurant.name shouldBe "칼국수"
                directions[1].restaurant.name shouldBe "중국요리"
                verify { restaurantService.searchRestaurantDtoList() }
            }
        }
    }

    Given("반경 10km에 있는 약국 1개와 밖에 있는 약국 1개가 주어졌을 때") {
        val restaurant1 = createRestaurantDto(1L, "만다린", 37.3825107393401, 127.236707811313)
        val restaurant2 = createRestaurantDto(2L, "장강", 37.61040424, 127.0569046)

        every { restaurantService.searchRestaurantDtoList() }.returns(listOf(restaurant2, restaurant1))
        When("경로를 조회를 한다면") {
            val directions = directionService.buildDirectionList(document)
            Then("음식점 1개만 조회된다.") {
                directions.size shouldBe 1
                directions[0].restaurant.name shouldBe "장강"
                verify { restaurantService.searchRestaurantDtoList() }
            }
        }
    }
})