package com.midas.restaurant.restaurant.controller

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.restaurant.service.RecommendationService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class RecommendationControllerTest : DescribeSpec({

    val recommendationService = mockk<RecommendationService>()
    val recommendationController = RecommendationController(recommendationService)
    val mvc = MockMvcBuilders.standaloneSetup(recommendationController)
        .build()

    describe("[GET] /recommendations - 추천 음식점 조회") {
        every { recommendationService.recommendRestaurantList(any(String::class)) }.returns(listOf())
        it("정상호출") {
            mvc.perform(
                get("/recommendations")
                    .queryParam("address", "경기도")
            ).andExpect { status().isOk }
                .andExpect { jsonPath("$.code").value(ResultStatus.SUCCESS.code) }
                .andExpect(jsonPath("$.message").value(ResultStatus.SUCCESS.message))
            verify { recommendationService.recommendRestaurantList(any(String::class)) }
        }
    }
}) {
}