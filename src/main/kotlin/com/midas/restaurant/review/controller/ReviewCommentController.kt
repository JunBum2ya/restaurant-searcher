package com.midas.restaurant.review.controller

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.common.dto.response.CommonResponse
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.review.dto.request.CommentRequest
import com.midas.restaurant.review.dto.response.CommentResponse
import com.midas.restaurant.review.service.CommentService
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/review/{reviewId}/comment")
class ReviewCommentController(private val commentService: CommentService) {

    private val log = LoggerFactory.getLogger(ReviewCommentController::class.java)

    @GetMapping
    fun searchComments(
        @PathVariable reviewId: Long,
        pageable: Pageable
    ): ResponseEntity<CommonResponse<Page<CommentResponse>>> {
        val page = commentService.searchComments(reviewId, pageable).map { CommentResponse.from(it) }
        return ResponseEntity.ok(CommonResponse.of(page))
    }

    @GetMapping("/{commentId}")
    fun searchChildComments(
        @PathVariable reviewId: Long,
        @PathVariable commentId: Long,
        pageable: Pageable
    ): ResponseEntity<CommonResponse<Page<CommentResponse>>> {
        val page = commentService.searchChildComments(reviewId, commentId, pageable).map { CommentResponse.from(it) }
        return ResponseEntity.ok(CommonResponse.of(page))
    }

    @PostMapping
    fun postComment(
        @PathVariable reviewId: Long,
        @Valid @RequestBody request: CommentRequest,
        @AuthenticationPrincipal principal: MemberDetails
    ): ResponseEntity<CommonResponse<CommentResponse>> {
        val comment = commentService.postComment(reviewId, principal.id, request.toDto())
        return ResponseEntity.ok(CommonResponse.of(CommentResponse.from(comment)))
    }

    @PostMapping("/{commentId}")
    fun postChildComment(
        @PathVariable reviewId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody request: CommentRequest,
        @AuthenticationPrincipal principal: MemberDetails
    ): ResponseEntity<CommonResponse<CommentResponse>> {
        val comment = commentService.postChildComment(commentId, principal.id, request.toDto())
        return ResponseEntity.ok(CommonResponse.of(CommentResponse.from(comment)))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable reviewId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody request: CommentRequest,
        @AuthenticationPrincipal principal: MemberDetails
    ): ResponseEntity<CommonResponse<CommentResponse>> {
        val comment = commentService.updateComment(commentId, principal.id, request.toDto())
        return ResponseEntity.ok(CommonResponse.of(CommentResponse.from(comment)))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable reviewId: Long,
        @PathVariable commentId: Long,
        @AuthenticationPrincipal principal: MemberDetails
    ): ResponseEntity<CommonResponse<Any>> {
        commentService.deleteComment(commentId, principal.id)
        return ResponseEntity.ok(CommonResponse.of(ResultStatus.SUCCESS))
    }

}