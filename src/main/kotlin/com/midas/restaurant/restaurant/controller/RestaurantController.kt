package com.midas.restaurant.restaurant.controller

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.common.dto.response.CommonResponse
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.restaurant.dto.RestaurantDetailDto
import com.midas.restaurant.restaurant.dto.request.RestaurantLikeRequest
import com.midas.restaurant.restaurant.dto.request.RestaurantRequest
import com.midas.restaurant.restaurant.dto.response.RestaurantDetailResponse
import com.midas.restaurant.restaurant.dto.response.RestaurantLikeResponse
import com.midas.restaurant.restaurant.dto.response.RestaurantResponse
import com.midas.restaurant.restaurant.service.RestaurantService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/restaurant")
class RestaurantController(private val restaurantService: RestaurantService) {

    @GetMapping
    fun searchRestaurant(
        @PageableDefault(
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<CommonResponse<Page<RestaurantResponse>>> {
        val page = restaurantService.searchRestaurantDtoList(pageable).map { RestaurantResponse.from(it) }
        return ResponseEntity.ok(CommonResponse.of(page))
    }

    @GetMapping("/{id}")
    fun getRestaurant(@PathVariable id: Long): ResponseEntity<CommonResponse<RestaurantDetailResponse>> {
        val restaurant = restaurantService.findRestaurantById(id)
        return ResponseEntity.ok(CommonResponse.of(RestaurantDetailResponse.from(restaurant)))
    }

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
    ): ResponseEntity<CommonResponse<Any>> {
        restaurantService.cancelLikeRestaurant(restaurantId, memberDetails.id)
        return ResponseEntity.ok(CommonResponse.of(ResultStatus.SUCCESS))
    }

}