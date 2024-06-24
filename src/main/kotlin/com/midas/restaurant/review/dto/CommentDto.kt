package com.midas.restaurant.review.dto

import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.review.domain.Comment
import java.time.LocalDateTime

class CommentDto(
    val id: Long? = null,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {

    companion object {
        fun from(comment: Comment): CommentDto {
            return CommentDto(
                id = comment.getId(),
                content = comment.getContent(),
                createdAt = comment.getCreatedAt(),
                updatedAt = comment.getUpdatedAt()
            )
        }
    }

    fun toEntity(author: Member): Comment {
        return Comment(author = author, content = content)
    }

}