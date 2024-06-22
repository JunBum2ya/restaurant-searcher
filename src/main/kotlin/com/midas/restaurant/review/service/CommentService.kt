package com.midas.restaurant.review.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.review.dto.CommentDto
import com.midas.restaurant.review.repository.CommentRepository
import com.midas.restaurant.review.repository.ReviewRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.Throws

@Service
class CommentService(
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository,
    private val commentRepository: CommentRepository
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(CustomException::class)
    @Transactional(readOnly = true)
    fun searchComments(reviewId: Long, pageable: Pageable): Page<CommentDto> {
        try {
            val review = reviewRepository.getReferenceById(reviewId)
            val page = commentRepository.findCommentsByReview(review, pageable)
            return page.map { CommentDto.from(it) }
        } catch (e: EntityNotFoundException) {
            log.error("Not found", e)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Throws(CustomException::class)
    @Transactional(readOnly = true)
    fun searchChildComments(reviewId: Long, commentId: Long, pageable: Pageable): Page<CommentDto> {
        try {
            return commentRepository.findCommentsByParentCommentId(commentId, pageable).map { CommentDto.from(it) }
        } catch (e: EntityNotFoundException) {
            log.error("Not found", e)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Throws(CustomException::class)
    @Transactional
    fun postComment(reviewId: Long, authorId: Long, commentDto: CommentDto): CommentDto {
        try {
            val review = reviewRepository.getReferenceById(reviewId)
            val author = memberRepository.getReferenceById(authorId)
            val comment = commentDto.toEntity(author)
            review.addComment(comment)
            return CommentDto.from(comment)
        } catch (e: EntityNotFoundException) {
            log.error(e.message)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Throws(CustomException::class)
    @Transactional
    fun postChildComment(commentId: Long, authorId: Long, commentDto: CommentDto): CommentDto {
        try {
            val parentComment = commentRepository.getReferenceById(commentId)
            val author = memberRepository.getReferenceById(authorId)
            val comment = commentDto.toEntity(author)
            parentComment.addChildComment(comment)
            return CommentDto.from(comment)
        } catch (e: EntityNotFoundException) {
            log.error(e.message)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    @Throws(CustomException::class)
    @Transactional
    fun updateComment(commentId: Long, authorId: Long, commentDto: CommentDto): CommentDto {
        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        if (comment.author.getId() != authorId) {
            throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
        }
        comment.update(content = commentDto.content)
        return CommentDto.from(comment)
    }

    @Throws(CustomException::class)
    @Transactional
    fun deleteComment(commentId: Long, authorId: Long) {
        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        if (comment.author.getId() != authorId) {
            throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
        }
        commentRepository.delete(comment)
    }

}