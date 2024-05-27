package com.midas.restaurant.restaurant.service

import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.mockito.Mockito.mock

class RestaurantServiceTest : BehaviorSpec({

    val restaurantRepository = mockk<RestaurantRepository>()
    val restaurantService = RestaurantService(restaurantRepository)

    Given("아무것도 주어지지 않았을 때") {
        val restaurant = Restaurant(
            id = 1,
            name = "test",
            address = "test",
            roadAddressName = "test",
            latitude = 0.0,
            longitude = 0.0,
            phoneNumber = "test",
            websiteUrl = "test"
        )
        every { restaurantRepository.findAll() } returns listOf(restaurant)
        When("레스토랑 목록을 조회하면") {
            val restaurantList = restaurantService.searchRestaurantDtoList()
            Then("레스토랑 리스트가 반환된다.") {
                restaurantList.size shouldBe 1
                verify { restaurantRepository.findAll() }
            }
        }
    }
}) {
}