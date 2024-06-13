package com.midas.restaurant.review.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import com.midas.restaurant.review.dto.ReviewDto
import com.midas.restaurant.review.dto.ReviewDetailDto
import com.midas.restaurant.review.repository.ReviewRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository,
    private val restaurantRepository: RestaurantRepository
) {

    private val log = LoggerFactory.getLogger(ReviewService::class.java)

    @Transactional(readOnly = true)
    fun searchReviews(pageable: Pageable): Page<ReviewDto> {
        val page = reviewRepository.findAll(pageable)
        return page.map { ReviewDto.from(it) }
    }

    @Transactional(readOnly = true)
    fun findReviewDetails(reviewId: Long): ReviewDetailDto {
        try {
            val review = reviewRepository.getReferenceById(reviewId)
            return ReviewDetailDto.from(review)
        }catch (e: EntityNotFoundException){
            log.warn("Review with id: $reviewId does not exists.")
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Transactional
    fun createReview(reviewDto: ReviewDto, authorId: Long, restaurantId: Long): ReviewDto {
        try {
            val author = memberRepository.getReferenceById(authorId)
            val restaurant = restaurantRepository.getReferenceById(restaurantId)
            val review = reviewRepository.save(reviewDto.toEntity(restaurant, author))
            return ReviewDto.from(review)
        } catch (e: EntityNotFoundException) {
            log.error(e.message, e)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Transactional
    fun updateReview(reviewId: Long, reviewDto: ReviewDto, authorId: Long): ReviewDetailDto {
        try {
            val review = reviewRepository.getReferenceById(reviewId)
            if(review.author.getId() != authorId) {
                throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
            }
            review.update(title = reviewDto.title, content = reviewDto.content)
            return ReviewDetailDto.from(review)
        } catch (e: EntityNotFoundException) {
            log.error(e.message, e)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Transactional
    fun deleteReview(reviewId: Long, authorId: Long) {
        try {
            val review = reviewRepository.getReferenceById(reviewId)
            if(review.author.getId() != authorId) {
                throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
            }
            reviewRepository.delete(review)
        }catch (e: EntityNotFoundException){
            log.error(e.message, e)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

}