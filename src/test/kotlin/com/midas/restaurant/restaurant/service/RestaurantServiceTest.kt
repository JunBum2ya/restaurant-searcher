package com.midas.restaurant.restaurant.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.domain.RestaurantLike
import com.midas.restaurant.restaurant.domain.cache.RestaurantCache
import com.midas.restaurant.restaurant.dto.RestaurantLikeDto
import com.midas.restaurant.restaurant.repository.RestaurantLikeRepository
import com.midas.restaurant.restaurant.repository.RestaurantRedisRepository
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.mockito.Mockito.mock

class RestaurantServiceTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerTest

    val restaurantRepository = mockk<RestaurantRepository>()
    val restaurantRedisRepository = mockk<RestaurantRedisRepository>()
    val memberRepository = mockk<MemberRepository>()
    val restaurantLikeRepository = mockk<RestaurantLikeRepository>()
    val restaurantService =
        RestaurantService(restaurantRepository, restaurantRedisRepository, restaurantLikeRepository, memberRepository)

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
    Given("저장되어 있는 Restaurant과 저장되어 있는 Member가 있을 경우") {
        val restaurantId = 1L
        val memberId = 1L
        val restaurantLikeParam = RestaurantLikeDto(stars = 3.4f)
        every { restaurantRepository.getReferenceById(any(Long::class)) }.returns(buildRestaurant())
        every { memberRepository.getReferenceById(any(Long::class)) }.returns(buildMember())
        every { restaurantLikeRepository.save(any(RestaurantLike::class)) }.returns(buildRestaurantLike())
        When("좋아요를 한번도 누르지 않은 상태에서 좋아요를 실행했다면") {
            every {
                restaurantLikeRepository.findRestaurantLikeByRestaurantAndMember(
                    any(Restaurant::class),
                    any(Member::class)
                )
            }.returns(null)
            val restaurantLikeDto = restaurantService.likeRestaurant(restaurantId, memberId, restaurantLikeParam)
            Then("레스트랑 좋아요 결과가 반환된다.") {
                restaurantLikeDto.stars shouldBe restaurantLikeParam.stars
            }
            Then("Member를 조회한다.") {
                verify { memberRepository.getReferenceById(any(Long::class)) }
            }
            Then("음식점을 조회한다.") {
                verify { restaurantRepository.getReferenceById(any(Long::class)) }
            }
            Then("기존에 좋아요를 눌렀는지 조회한다.") {
                verify {
                    restaurantLikeRepository.findRestaurantLikeByRestaurantAndMember(
                        any(Restaurant::class),
                        any(Member::class)
                    )
                }
            }
            Then("DB에 좋아요를 저장한다.") {
                verify { restaurantLikeRepository.save(any(RestaurantLike::class)) }
            }
        }
        When("이미 좋아요를 누른 상태에서 좋아요를 실행했다면") {
            every {
                restaurantLikeRepository.findRestaurantLikeByRestaurantAndMember(
                    any(Restaurant::class),
                    any(Member::class)
                )
            }.returns(buildRestaurantLike())
            val restaurantLikeDto = restaurantService.likeRestaurant(restaurantId, memberId, restaurantLikeParam)
            Then("레스토랑 좋아요 결과가 반환된다.") {
                restaurantLikeDto.stars shouldBe restaurantLikeParam.stars
            }
            Then("Member를 조회한다.") {
                verify { memberRepository.getReferenceById(any(Long::class)) }
            }
            Then("음식점을 조회한다.") {
                verify { restaurantRepository.getReferenceById(any(Long::class)) }
            }
            Then("기존에 좋아요를 눌렀는지 조회한다.") {
                verify {
                    restaurantLikeRepository.findRestaurantLikeByRestaurantAndMember(
                        any(Restaurant::class),
                        any(Member::class)
                    )
                }
            }
        }
    }
    Given("저장되어 있지 않은 Restaurant과 저장되어 있는 Member가 있을 경우") {
        val restaurantId = 1L
        val memberId = 1L
        val restaurantLikeParam = RestaurantLikeDto(stars = 3.4f)
        every { restaurantRepository.getReferenceById(any(Long::class)) }
            .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
        When("좋아요를 실행했다면") {
            val exception = shouldThrow<CustomException> {
                restaurantService.likeRestaurant(
                    memberId,
                    restaurantId,
                    restaurantLikeParam
                )
            }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("음식점 조회를 실행한다.") {
                verify { restaurantRepository.getReferenceById(any(Long::class)) }
            }
        }
    }
    Given("저장되어 있는 Restaurant과 저장되어 있지 않은 Member가 있을 경우") {
        val restaurantId = 1L
        val memberId = 1L
        val restaurantLikeParam = RestaurantLikeDto(stars = 3.4f)
        every { restaurantRepository.getReferenceById(any(Long::class)) }.returns(buildRestaurant())
        every { memberRepository.getReferenceById(any(Long::class)) }
            .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
        When("좋아요를 실행했다면") {
            val exception = shouldThrow<CustomException> {
                restaurantService.likeRestaurant(
                    memberId,
                    restaurantId,
                    restaurantLikeParam
                )
            }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("음식점을 조회한다") {
                verify { restaurantRepository.getReferenceById(any(Long::class)) }
            }
            Then("멤버를 조회한다.") {
                verify { memberRepository.getReferenceById(any(Long::class)) }
            }
        }
    }
}) {

    companion object {
        fun buildRestaurant() = Restaurant(
            id = 1L,
            name = "test",
            address = "test",
            roadAddressName = "test",
            latitude = 0.0,
            longitude = 0.0,
            phoneNumber = "test",
            websiteUrl = "test",
            owner = Member(2L, "master", "1234", "master@midas.com")
        )

        fun buildMember() = Member(id = 1L, username = "test", password = "1234", email = "test@test.com")


        fun buildRestaurantLike() =
            RestaurantLike(id = 2L, member = buildMember(), restaurant = buildRestaurant(), stars = 3.4f)
    }

}