package com.midas.restaurant.restaurant.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.common.component.TestAuthenticationPrincipal
import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.exception.CustomExceptionHandler
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.restaurant.dto.RestaurantLikeDto
import com.midas.restaurant.restaurant.dto.request.RestaurantLikeRequest
import com.midas.restaurant.restaurant.dto.request.RestaurantRequest
import com.midas.restaurant.restaurant.service.RestaurantService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class RestaurantControllerTest : DescribeSpec({

    val restaurantService = mockk<RestaurantService>()
    val restaurantController = RestaurantController(restaurantService)
    val objectMapper = ObjectMapper()
    val mvc = MockMvcBuilders
        .standaloneSetup(restaurantController, CustomExceptionHandler())
        .setCustomArgumentResolvers(TestAuthenticationPrincipal())
        .build()

    describe("음식점 저장 API 테스트") {
        context("정상적으로 음식점을 저장") {
            val request = RestaurantRequest(
                address = "test",
                roadAddressName = "test",
                name = "test",
                phoneNumber = "test",
                websiteUrl = "test",
                latitude = 0.0,
                longitude = 0.0
            )
            every { restaurantService.saveRestaurant(any(RestaurantDto::class), any(Long::class)) }
                .returns(request.toDto())
            it("200 OK") {
                mvc.perform(
                    post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isOk)
                    .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                    .andExpect(jsonPath("data.address").value(request.address))
                    .andExpect(jsonPath("data.name").value(request.name))
                verify { restaurantService.saveRestaurant(any(RestaurantDto::class), any(Long::class)) }
            }
        }
    }

    describe("좋아요 테스트") {
        val restaurantId = 1L
        val request = RestaurantLikeRequest(stars = "4.3")
        context("좋아요가 성공했을 경우") {
            every {
                restaurantService.likeRestaurant(
                    any(Long::class),
                    any(Long::class),
                    any(RestaurantLikeDto::class)
                )
            }.returns(request.toDto())
            it("200 OK 호출") {
                mvc.perform(
                    post("/api/v1/restaurant/${restaurantId}/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                    .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
            }
            it("좋아요 저장 실행") {
                verify {
                    restaurantService.likeRestaurant(
                        any(Long::class),
                        any(Long::class),
                        any(RestaurantLikeDto::class)
                    )
                }
            }
        }
        context("음식점 저장에 실패했을 경우") {
            every {
                restaurantService.likeRestaurant(
                    any(Long::class),
                    any(Long::class),
                    any(RestaurantLikeDto::class)
                )
            }.throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
            it("404 ERROR 호출") {
                mvc.perform(
                    post("/api/v1/restaurant/${restaurantId}/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                    .andExpect(status().isBadRequest)
                    .andExpect(jsonPath("code").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.code))
                    .andExpect(jsonPath("message").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.message))
            }
            it("좋아요 저장 실행") {
                verify {
                    restaurantService.likeRestaurant(
                        any(Long::class),
                        any(Long::class),
                        any(RestaurantLikeDto::class)
                    )
                }
            }
        }
    }

})