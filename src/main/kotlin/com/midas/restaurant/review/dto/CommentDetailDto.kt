package com.midas.restaurant.review.dto

import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.review.domain.Comment
import java.time.LocalDateTime

data class CommentDetailDto(
    val id: Long? = null,
    val author: MemberDto,
    val content: String,
    val childComments: List<CommentDto>,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(comment: Comment): CommentDetailDto {
            return CommentDetailDto(
                id = comment.getId(),
                author = MemberDto.from(comment.author),
                content = comment.getContent(),
                childComments = comment.getChildComments().map { CommentDto.from(it) },
                createdAt = comment.getCreatedAt(),
                updatedAt = comment.getUpdatedAt()
            )
        }
    }
}