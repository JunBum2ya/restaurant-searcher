package com.midas.restaurant.review.controller

import com.midas.restaurant.exception.CustomExceptionHandler
import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.review.dto.ReviewDetailDto
import com.midas.restaurant.review.service.ReviewService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ReviewControllerTest : DescribeSpec({

    val reviewService = mockk<ReviewService>()
    val reviewController = ReviewController(reviewService)
    val mvc = MockMvcBuilders.standaloneSetup(reviewController, CustomExceptionHandler()).build()

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
        }
    }

}) {
}