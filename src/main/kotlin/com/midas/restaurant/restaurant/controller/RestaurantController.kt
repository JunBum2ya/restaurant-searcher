package com.midas.restaurant.restaurant.controller

import com.midas.restaurant.common.dto.response.CommonResponse
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.restaurant.dto.request.RestaurantLikeRequest
import com.midas.restaurant.restaurant.dto.request.RestaurantRequest
import com.midas.restaurant.restaurant.dto.response.RestaurantLikeResponse
import com.midas.restaurant.restaurant.dto.response.RestaurantResponse
import com.midas.restaurant.restaurant.service.RestaurantService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/restaurant")
class RestaurantController(private val restaurantService: RestaurantService) {

    @PostMapping
    fun createRestaurant(
        @Valid @RequestBody request: RestaurantRequest,
        @AuthenticationPrincipal memberDetails: MemberDetails
    ): ResponseEntity<CommonResponse<RestaurantResponse>> {
        val restaurant = restaurantService.saveRestaurant(request.toDto(), memberDetails.id)
        return ResponseEntity.ok(CommonResponse.of(RestaurantResponse.from(restaurant)))
    }

    @PostMapping("/{restaurantId}/likes")
    fun likeRestaurant(
        @PathVariable restaurantId: Long,
        @Valid @RequestBody request: RestaurantLikeRequest,
        @AuthenticationPrincipal memberDetails: MemberDetails
    ): ResponseEntity<CommonResponse<RestaurantLikeResponse>> {
        val restaurantLikeDto = restaurantService.likeRestaurant(restaurantId, memberDetails.id, request.toDto())
        return ResponseEntity.ok(
            CommonResponse.of(
                RestaurantLikeResponse.from(
                    restaurantId = restaurantId,
                    restaurantLikeDto = restaurantLikeDto
                )
            )
        )
    }

    @DeleteMapping("/{restaurantId}/likes")
    fun cancelLikeRestaurant(
        @PathVariable restaurantId: Long,
        @AuthenticationPrincipal memberDetails: MemberDetails
    ): ResponseEntity<CommonResponse<RestaurantLikeResponse>> {
        val restaurantLike = restaurantService.cancelLikeRestaurant(restaurantId, memberDetails.id)
        return ResponseEntity.ok(CommonResponse.of(RestaurantLikeResponse.from(restaurantId, restaurantLike)))
    }

}