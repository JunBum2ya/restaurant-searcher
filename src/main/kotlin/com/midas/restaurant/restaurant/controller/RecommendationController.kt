package com.midas.restaurant.restaurant.controller

import com.midas.restaurant.restaurant.dto.RestaurantDto
import com.midas.restaurant.restaurant.service.RecommendationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recommendations")
class RecommendationController(private val recommendationService: RecommendationService) {

    @GetMapping
    fun recommendRestaurantList(@RequestParam address: String): List<RestaurantDto> {
        return recommendationService.recommendRestaurantList(address)
    }

}