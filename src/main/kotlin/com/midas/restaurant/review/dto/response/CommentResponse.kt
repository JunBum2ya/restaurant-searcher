package com.midas.restaurant.review.dto.response

import com.midas.restaurant.review.dto.CommentDto
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long? = null,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(comment: CommentDto): CommentResponse {
            return CommentResponse(
                id = comment.id,
                content = comment.content,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
        }
    }
}
