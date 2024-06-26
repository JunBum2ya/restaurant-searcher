package com.midas.restaurant.review.service

import com.midas.restaurant.alarm.domain.contant.AlarmType
import com.midas.restaurant.alarm.service.AlarmService
import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.config.TestJwtTokenProvider
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import com.midas.restaurant.review.domain.Review
import com.midas.restaurant.review.dto.ReviewDetailDto
import com.midas.restaurant.review.dto.ReviewDto
import com.midas.restaurant.review.repository.ReviewRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull

class ReviewServiceTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val reviewRepository = mockk<ReviewRepository>()
    val memberRepository = mockk<MemberRepository>()
    val restaurantRepository = mockk<RestaurantRepository>()
    val alarmService = mockk<AlarmService>()
    val reviewService = ReviewService(reviewRepository, memberRepository, restaurantRepository, alarmService)

    Given("pageable이 주어졌을 때") {
        val pageable = PageRequest.of(0, 10)
        every { reviewRepository.findAll(any(Pageable::class)) } returns PageImpl(listOf(buildReview()), pageable, 1)
        When("리뷰를 조회한다면") {
            val page = reviewService.searchReviews(pageable)
            Then("페이지가 반환된다.") {
                page shouldHaveSize 1
                verify { reviewRepository.findAll(any(Pageable::class)) }
            }
        }
    }
    Given("리뷰 아이디가 주어졌을 때") {
        val reviewId = 10L
        When("리뷰를 상세 조회를 하면") {
            every { reviewRepository.getReferenceById(any(Long::class)) } returns buildReview()
            val reviewData = reviewService.findReviewDetails(reviewId)
            Then("리뷰가 반환된다.") {
                reviewData shouldNotBe null
                reviewData.id shouldBe 1
                reviewData.restaurant.id shouldBe 1
                reviewData.author.id shouldBe 1
                verify(exactly = 1) { reviewRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("존재하지 않는 리뷰를 상세 조회를 하면") {
            every { reviewRepository.getReferenceById(any(Long::class)) } throws EntityNotFoundException()
            val exception = shouldThrow<CustomException> { reviewService.findReviewDetails(reviewId) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
        }
    }
    Given("글쓴이 아이디와 음식점 아이디 그리고 리뷰 데이터가 주어졌을 때") {
        val authorId = 1L
        val restaurantId = 10L
        val reviewData = ReviewDto(title = "title", content = "content")
        every { alarmService.send(any(String::class), any(AlarmType::class), any(String::class)) }.returns(Unit)
        every { reviewRepository.save(any(Review::class)) } returns buildReview()
        When("음식점과 글쓴이가 모두 있어 저장한다면") {
            every { restaurantRepository.findByIdOrNull(any(Long::class)) } returns buildRestaurant()
            every { memberRepository.findByIdOrNull(any(Long::class)) } returns buildMember()
            every { alarmService.send(any(String::class), any(AlarmType::class), any(String::class)) } returns Unit
            val review = reviewService.createReview(reviewData, authorId, restaurantId)
            Then("저장된 데이터가 반환된다.") {
                review.title shouldBe "title"
                review.content shouldBe "content"
            }
            Then("음식점을 조회한다.") {
                verify { restaurantRepository.findByIdOrNull(any(Long::class)) }
            }
            Then("글쓴이를 조회한다.") {
                verify { memberRepository.findByIdOrNull(any(Long::class)) }
            }
            Then("알람이 전송된다.") {
                verify { alarmService.send(any(String::class), any(AlarmType::class), any(String::class)) }
            }
        }
        When("음식점이 DB에 없을 때 저장을 한다면") {
            every { memberRepository.findByIdOrNull(any(Long::class)) } returns buildMember()
            every { restaurantRepository.findByIdOrNull(any(Long::class)) } returns null
            val exception =
                shouldThrow<CustomException> { reviewService.createReview(reviewData, authorId, restaurantId) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("글쓴이를 조회한다.") {
                verify { memberRepository.findByIdOrNull(any(Long::class)) }
            }
            Then("음식점을 조회한다.") {
                verify { restaurantRepository.findByIdOrNull(any(Long::class)) }
            }
        }
        When("글쓴이가 DB에 없을 때 저장을 한다면") {
            every { memberRepository.findByIdOrNull(any(Long::class)) } returns null
            val exception = shouldThrow<CustomException> { reviewService.createReview(reviewData, authorId, restaurantId) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("글쓴이를 조회한다.") {
                verify { memberRepository.findByIdOrNull(any(Long::class)) }
            }
        }
        When("알람 전송중에 오류가 발생했다면") {
            every { restaurantRepository.findByIdOrNull(any(Long::class)) } returns buildRestaurant()
            every { memberRepository.findByIdOrNull(any(Long::class)) } returns buildMember()
            every { alarmService.send(any(String::class), any(AlarmType::class), any(String::class)) }
                .throws(CustomException(ResultStatus.OCCUR_ALARM_ERROR))
            val exception = shouldThrow<CustomException> { reviewService.createReview(reviewData, authorId, restaurantId) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.OCCUR_ALARM_ERROR.code
                exception.message shouldBe ResultStatus.OCCUR_ALARM_ERROR.message
            }
            Then("음식점을 조회한다.") {
                verify { restaurantRepository.findByIdOrNull(any(Long::class)) }
            }
            Then("글쓴이를 조회한다.") {
                verify { memberRepository.findByIdOrNull(any(Long::class)) }
            }
            Then("알람을 전송한다.") {
                verify { alarmService.send(any(String::class), any(AlarmType::class), any(String::class)) }
            }
        }
    }
    Given("리뷰 아이디와 리뷰 데이터 그리고 저자 아이디가 주어졌을 때") {
        val reviewId = 10L
        val reviewData = ReviewDto(title = "update title", content = "update content")
        val authorId = 1L
        When("리뷰를 수정하는데 성공했다면") {
            every { reviewRepository.getReferenceById(any(Long::class)) } returns buildReview()
            val updatedReview = reviewService.updateReview(reviewId, reviewData, authorId)
            Then("수정된 리뷰 데이터가 반환된다.") {
                updatedReview shouldNotBe null
                updatedReview.title shouldBe reviewData.title
                updatedReview.content shouldBe reviewData.content
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("존재 하지 않는 리뷰를 수정했다면") {
            every { reviewRepository.getReferenceById(any(Long::class)) } throws EntityNotFoundException()
            val exception = shouldThrow<CustomException> { reviewService.updateReview(reviewId, reviewData, authorId) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("저자가 아닌 사용자가 리뷰를 수정했다면") {
            every { reviewRepository.getReferenceById(any(Long::class)) } returns buildReview()
            val exception = shouldThrow<CustomException> { reviewService.updateReview(reviewId, reviewData, 20L) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.UNAUTHENTICATED_USER.code
                exception.message shouldBe ResultStatus.UNAUTHENTICATED_USER.message
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
        }
    }
    Given("reviewId와 저자아이디가 주어졌을 때") {
        val reviewId = 10L
        val authorId = 1L
        every { reviewRepository.getReferenceById(any(Long::class)) } returns buildReview()
        every { reviewRepository.delete(any(Review::class)) } returns Unit
        When("리뷰 삭제에 성공했다면") {
            reviewService.deleteReview(reviewId, authorId)
            Then("아무것도 반환되지 않는다.") {
                verify { reviewRepository.getReferenceById(any(Long::class)) }
                verify { reviewRepository.delete(any(Review::class)) }
            }
        }
        When("저자가 아닌 리뷰를 삭제한다면") {
            val exception = shouldThrow<CustomException> { reviewService.deleteReview(reviewId, 20L) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.UNAUTHENTICATED_USER.code
                exception.message shouldBe ResultStatus.UNAUTHENTICATED_USER.message
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
        }
    }

}) {
    companion object {
        private fun buildReview() = Review(
            id = 1L,
            author = buildMember(),
            restaurant = buildRestaurant(),
            title = "title",
            content = "content"
        )

        private fun buildMember() = Member(id = 1L, username = "testUser", password = "1234", email = "test@test.com")
        private fun buildRestaurant() = Restaurant(
            id = 1L,
            name = "testRestaurant",
            address = "testAddress",
            roadAddressName = "testRoadAddress",
            latitude = 0.0,
            longitude = 0.0,
            phoneNumber = "testPhoneNumber",
            websiteUrl = "testWebsiteUrl",
            owner = buildMember()
        )
    }
}