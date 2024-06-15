package com.midas.restaurant.restaurant.controller

import com.midas.restaurant.common.dto.response.CommonResponse
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.restaurant.dto.response.RecommendedRestaurantResponse
import com.midas.restaurant.restaurant.service.RecommendationService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recommendations")
class RecommendationController(private val recommendationService: RecommendationService) {

    @GetMapping
    fun recommendRestaurantList(
        @RequestParam address: String,
        @AuthenticationPrincipal memberDetails: MemberDetails
    ): ResponseEntity<CommonResponse<List<RecommendedRestaurantResponse>>> {
        val restaurantList = recommendationService.recommendRestaurantList(address, memberDetails.id)
            .map { RecommendedRestaurantResponse.from(it) }
        return ResponseEntity.ok(CommonResponse.of(restaurantList))
    }

}