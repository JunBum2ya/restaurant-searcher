package com.midas.restaurant.restaurant.service

import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.domain.cache.RestaurantCache
import com.midas.restaurant.restaurant.repository.RestaurantRedisRepository
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.mockito.Mockito.mock

class RestaurantServiceTest : BehaviorSpec({

    val restaurantRepository = mockk<RestaurantRepository>()
    val restaurantRedisRepository = mockk<RestaurantRedisRepository>()
    val memberRepository = mockk<MemberRepository>()
    val restaurantService = RestaurantService(restaurantRepository, restaurantRedisRepository, memberRepository)

    Given("아무것도 주어지지 않았고 캐시에 데이터가 없을 때") {
        val restaurant = Restaurant(
            id = 1,
            name = "test",
            address = "test",
            roadAddressName = "test",
            latitude = 0.0,
            longitude = 0.0,
            phoneNumber = "test",
            websiteUrl = "test",
            owner = Member(1L, "master", "1234", "master@midas.com"),
        )
        every { restaurantRepository.findAll() } returns listOf(restaurant)
        every { restaurantRedisRepository.findAll() }.returns(emptyList())
        every { restaurantRedisRepository.save(any(RestaurantCache::class)) }.returns(
            RestaurantCache(
                id = 1,
                name = "test",
                address = "test",
                roadAddressName = "test",
                latitude = 0.0,
                longitude = 0.0,
                phoneNumber = "test",
                websiteUrl = "test"
            )
        )
        When("레스토랑 목록을 조회하면") {
            val restaurantList = restaurantService.searchRestaurantDtoList()
            Then("레스토랑 리스트가 반환된다.") {
                restaurantList.size shouldBe 1
                verify { restaurantRepository.findAll() }
                verify { restaurantRedisRepository.findAll() }
                verify { restaurantRedisRepository.save(any(RestaurantCache::class)) }
            }
        }
    }
    Given("아무것도 주어지지 않았고 캐시에 데이터가 있을 때") {
        val restaurant = RestaurantCache(
            id = 1,
            name = "test",
            address = "test",
            roadAddressName = "test",
            latitude = 0.0,
            longitude = 0.0,
            phoneNumber = "test",
            websiteUrl = "test"
        )
        every { restaurantRedisRepository.findAll() }.returns(listOf(restaurant))
        When("레스토랑 목록을 조회하면") {
            val restaurantList = restaurantService.searchRestaurantDtoList()
            Then("레스토랑 리스트가 반환된다.") {
                restaurantList.size shouldBe 1
                verify { restaurantRedisRepository.findAll() }
            }
        }
    }
}) {
}