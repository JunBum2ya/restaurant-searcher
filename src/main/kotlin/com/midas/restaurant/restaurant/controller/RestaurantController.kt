package com.midas.restaurant.restaurant.controller

import com.midas.restaurant.common.dto.response.CommonResponse
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.restaurant.dto.request.RestaurantRequest
import com.midas.restaurant.restaurant.dto.response.RestaurantResponse
import com.midas.restaurant.restaurant.service.RestaurantService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}