package com.midas.restaurant.review.repository;

import com.midas.restaurant.review.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
}