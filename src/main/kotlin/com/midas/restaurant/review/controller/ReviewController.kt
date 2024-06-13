package com.midas.restaurant.review.controller

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.common.dto.response.CommonResponse
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.review.dto.request.ReviewRequest
import com.midas.restaurant.review.dto.response.ReviewDetailResponse
import com.midas.restaurant.review.dto.response.ReviewResponse
import com.midas.restaurant.review.dto.response.SimpleReviewResponse
import com.midas.restaurant.review.service.ReviewService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/review")
class ReviewController(private val reviewService: ReviewService) {

    fun searchReview(
        @PageableDefault(size = 10, sort = ["registeredDate"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ResponseEntity<CommonResponse<Page<SimpleReviewResponse>>> {
        val page = reviewService.searchReviews(pageable)
        return ResponseEntity.ok(CommonResponse.of(page.map { SimpleReviewResponse.from(it) }))
    }

    @GetMapping("/{reviewId}")
    fun searchReviewDetail(@PathVariable reviewId: Long): ResponseEntity<CommonResponse<ReviewDetailResponse>> {
        val review = reviewService.findReviewDetails(reviewId)
        return ResponseEntity.ok(CommonResponse.of(ReviewDetailResponse.from(review)))
    }

    @PostMapping
    fun createReview(
        @Valid @RequestBody request: ReviewRequest,
        authentication: Authentication
    ): ResponseEntity<CommonResponse<ReviewResponse>> {
        val review = reviewService.createReview(
            reviewDto = request.toDto(),
            restaurantId = request.restaurantId,
            authorId = 1L
        )
        return ResponseEntity.ok(
            CommonResponse.of(
                ReviewResponse.from(
                    review = review,
                    restaurantId = request.restaurantId,
                    authorId = 1L
                )
            )
        )
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable reviewId: Long,
        @Valid @RequestBody request: ReviewRequest,
        authentication: Authentication
    ): ResponseEntity<CommonResponse<ReviewResponse>> {
        val review = reviewService.updateReview(reviewId, request.toDto())
        return ResponseEntity.ok(CommonResponse.of(ReviewResponse.from(review)))
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable reviewId: Long,
        authentication: Authentication
    ): ResponseEntity<CommonResponse<Any>> {
        reviewService.deleteReview(reviewId)
        return ResponseEntity.ok(CommonResponse.of(ResultStatus.SUCCESS))
    }

}