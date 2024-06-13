package com.midas.restaurant.review.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.common.TestAuthenticationPrincipal
import com.midas.restaurant.common.component.JwtTokenProvider
import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.config.SecurityConfig
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.exception.CustomExceptionHandler
import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.review.dto.ReviewDetailDto
import com.midas.restaurant.review.dto.ReviewDto
import com.midas.restaurant.review.dto.request.ReviewRequest
import com.midas.restaurant.review.service.ReviewService
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ReviewControllerTest : DescribeSpec({

    val reviewService = mockk<ReviewService>()
    val reviewController = ReviewController(reviewService)
    val objectMapper = ObjectMapper()
    val mvc = MockMvcBuilders
        .standaloneSetup(reviewController)
        .setControllerAdvice(CustomExceptionHandler())
        .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver(), TestAuthenticationPrincipal())
        .build()

    describe("리뷰 목록 페이지 조회") {
        context("조회 성공 시") {
            val page: Page<ReviewDto> = PageImpl(mutableListOf(buildReviewDto()),PageRequest.of(0, 10), 1)
            every { reviewService.searchReviews(any(Pageable::class)) }.returns(page)
            it("200 OK") {
                mvc.perform(get("/api/v1/review"))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                    .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                    .andExpect(jsonPath("data").isMap)
                verify { reviewService.searchReviews(any(Pageable::class)) }
            }
        }
    }

    describe("리뷰 상세 조회 성공 시") {
        every { reviewService.findReviewDetails(any(Long::class)) }
            .returns(
                ReviewDetailDto(
                    id = 1L,
                    restaurant = RestaurantDto(
                        id = 1L,
                        name = "test",
                        address = "test",
                        latitude = 0.0,
                        longitude = 0.0,
                        phoneNumber = "010-1234-5678",
                        websiteUrl = "www.naver.com",
                        roadAddressName = "test"
                    ),
                    author = MemberDto(id = 1L, username = "test", password = "1234", email = "test@test.com"),
                    title = "test",
                    content = "test"
                )
            )
        it("200 OK") {
            mvc.perform(get("/api/v1/review/1"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                .andExpect(jsonPath("data").isMap)
            verify { reviewService.findReviewDetails(any(Long::class)) }
        }
    }

    describe("리뷰 생성") {
        val request = ReviewRequest(restaurantId = 1L, title = "title", content = "content")
        context("생성 성공 시") {
            every { reviewService.createReview(any(ReviewDto::class), any(Long::class), any(Long::class)) }
                .returns(buildReviewDto())
            it("200 OK") {
                mvc.perform(
                    post("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                    .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                    .andExpect(jsonPath("data").isMap)
                verify { reviewService.createReview(any(ReviewDto::class), any(Long::class), any(Long::class)) }
            }
        }
        context("생성 실패 시") {
            every { reviewService.createReview(any(ReviewDto::class), any(Long::class), any(Long::class)) }
                .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
            it("예외 메시지 발생") {
                mvc.perform(
                    post("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                    .andExpect(status().isBadRequest)
                    .andExpect(jsonPath("code").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.code))
                    .andExpect(jsonPath("message").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.message))
                    .andExpect(jsonPath("data").isEmpty)
                verify { reviewService.createReview(any(ReviewDto::class), any(Long::class), any(Long::class)) }
            }
        }
    }
    describe("리뷰 수정") {
        val reviewId = 1L
        val request = ReviewRequest(restaurantId = 1L, title = "title", content = "content")
        context("수정 성공 시") {
            every { reviewService.updateReview(any(Long::class), any(ReviewDto::class), any(Long::class)) }
                .returns(buildReviewDetailDto())
            it("200 OK") {
                mvc.perform(
                    put("/api/v1/review/${reviewId}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                    .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                    .andExpect(jsonPath("data").isMap)
                verify { reviewService.updateReview(any(Long::class), any(ReviewDto::class), any(Long::class)) }
            }
        }
        context("수정 실패 시") {
            every { reviewService.updateReview(any(Long::class), any(ReviewDto::class), any(Long::class)) }
                .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
            it("400 에러 발생") {
                mvc.perform(
                    put("/api/v1/review/${reviewId}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest)
                    .andExpect(jsonPath("code").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.code))
                    .andExpect(jsonPath("message").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.message))
                    .andExpect(jsonPath("data").isEmpty)
                verify { reviewService.updateReview(any(Long::class), any(ReviewDto::class), any(Long::class)) }
            }
        }
    }
    describe("리뷰 삭제") {
        val reviewId = 1L
        context("삭제 성공 시") {
            every { reviewService.deleteReview(any(Long::class), any(Long::class)) }.returns(Unit)
            it("200 OK") {
                mvc.perform(
                    delete("/api/v1/review/${reviewId}"))
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                    .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                    .andExpect(jsonPath("data").isEmpty)
            }
        }
        context("삭제 실패 시") {
            every { reviewService.deleteReview(any(Long::class), any(Long::class)) }
                .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
            it("400 에러 발생") {
                mvc.perform(delete("/api/v1/review/${reviewId}"))
                    .andExpect(status().isBadRequest)
                    .andExpect(jsonPath("code").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.code))
                    .andExpect(jsonPath("message").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.message))
                    .andExpect(jsonPath("data").isEmpty)
                verify { reviewService.deleteReview(any(Long::class), any(Long::class)) }
            }
        }
    }

}) {
    companion object {
        fun buildReviewDto(): ReviewDto {
            return ReviewDto(id = 1L, title = "test", content = "content")
        }

        fun buildReviewDetailDto(): ReviewDetailDto {
            return ReviewDetailDto(
                id = 1L,
                title = "test",
                content = "content",
                restaurant = RestaurantDto(
                    id = 1,
                    name = "test",
                    address = "test",
                    latitude = 0.0,
                    longitude = 0.0,
                    phoneNumber = "010-1234-5678",
                    roadAddressName = "test",
                    websiteUrl = "www.naver.com",
                ),
                author = MemberDto(id = 1L, username = "test", password = "1234", email = "test@test.com")
            )
        }
    }
}