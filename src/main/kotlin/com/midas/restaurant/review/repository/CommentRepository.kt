package com.midas.restaurant.review.repository;

import com.midas.restaurant.review.domain.Comment
import com.midas.restaurant.review.domain.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findCommentsByReview(review: Review, pageable: Pageable): Page<Comment>
    fun findCommentsByParentCommentId(parentCommentId: Long, pageable: Pageable): Page<Comment>
}