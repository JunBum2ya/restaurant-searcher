package com.midas.restaurant.review.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.review.domain.Comment
import com.midas.restaurant.review.domain.Review
import com.midas.restaurant.review.dto.CommentDto
import com.midas.restaurant.review.repository.CommentRepository
import com.midas.restaurant.review.repository.ReviewRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull

class CommentServiceTest : BehaviorSpec({
    val reviewRepository = mockk<ReviewRepository>()
    val memberRepository = mockk<MemberRepository>()
    val commentRepository = mockk<CommentRepository>()
    val commentService = CommentService(reviewRepository, memberRepository, commentRepository)

    Given("리뷰가 저장되어 있을 때") {
        val reviewId = 1L
        val pageable = PageRequest.of(0, 10)
        val comment = Comment(id = 1L, review = buildReview(), author = buildMember(), content = "text")
        every { reviewRepository.getReferenceById(any(Long::class)) }.returns(buildReview())
        every { commentRepository.findCommentsByReview(any(Review::class), any(Pageable::class)) }
            .returns(PageImpl(mutableListOf(comment), pageable, 1L))
        When("리뷰의 댓글을 조회하면") {
            val page = commentService.searchComments(reviewId, pageable)
            Then("댓글 페이지가 반환된다.") {
                page.number shouldBe 0
                page.size shouldBe 10
                page.totalPages shouldBe 1
                page.content shouldHaveSize 1
            }
            Then("리뷰를 조회한다.") {
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
            Then("댓글을 조회한다.") {
                verify { commentRepository.findCommentsByReview(any(Review::class), any(Pageable::class)) }
            }
        }
    }

    Given("리뷰가 존재하지 않을 때") {
        val reviewId = 1L
        val pageable = PageRequest.of(0, 10)
        every { reviewRepository.getReferenceById(any(Long::class)) }.throws(EntityNotFoundException())
        When("리뷰의 댓글을 조회하면") {
            val exception = shouldThrow<CustomException> { commentService.searchComments(reviewId, pageable) }
            Then("예외가 발생한다..") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("리뷰를 조회한다.") {
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
        }
    }

    Given("댓글 아이디가 주어졌을 때") {
        val reviewId = 1L
        val commentId = 1L
        val pageable = PageRequest.of(0, 10)
        every { commentRepository.findCommentsByParentCommentId(any(Long::class), any(Pageable::class)) }
            .returns(PageImpl(mutableListOf<Comment>(), pageable, 1L))
        When("대댓글을 조회한다면") {
            val page = commentService.searchChildComments(reviewId, commentId, pageable)
            Then("대댓글 페이지가 조회된다.") {
                page.number shouldBe pageable.pageNumber
                page.size shouldBe pageable.pageSize
                page.content shouldHaveSize 0
            }
        }
    }

    Given("리뷰 아이디와 글쓴이 아이디, 댓글 데이터가 주어졌을 때") {
        val reviewId = 1L
        val authorId = 1L
        val commentDto = CommentDto(content = "content")
        When("리뷰와 글쓴이가 실제로 있을 경우 저장한다면") {
            val review = buildReview()
            every { reviewRepository.getReferenceById(any(Long::class)) }.returns(review)
            every { memberRepository.getReferenceById(any(Long::class)) }.returns(buildMember())
            val newComment = commentService.postComment(reviewId, authorId, commentDto)
            Then("댓글 데이터가 반환된다.") {
                newComment.content shouldBe commentDto.content
            }
            Then("리뷰에 댓글이 추가된다.") {
                review.getComments().size shouldBe 1
            }
            Then("리뷰를 조회한다.") {
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
            Then("글쓴이를 조회한다.") {
                verify { memberRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("리뷰가 없을 때 저장한다면") {
            every { reviewRepository.getReferenceById(any(Long::class)) }.throws(EntityNotFoundException())
            val exception = shouldThrow<CustomException> { commentService.postComment(reviewId, authorId, commentDto) }
            Then("예외가 발생한다") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("리뷰를 조회한다.") {
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("글쓴이가 없을 때 저장한다면") {
            every { reviewRepository.getReferenceById(any(Long::class)) }.returns(buildReview())
            every { memberRepository.getReferenceById(any(Long::class)) }.throws(EntityNotFoundException())
            val exception = shouldThrow<CustomException> { commentService.postComment(reviewId, authorId, commentDto) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("리뷰를 조회한다.") {
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
            Then("글쓴이를 조회한다.") {
                verify { memberRepository.getReferenceById(any(Long::class)) }
            }
        }
    }

    Given("댓글 아이디와 글쓴이 아이디, 댓글 데이터가 주어졌을 때") {
        val commentId = 1L
        val authorId = 1L
        val commentDto = CommentDto(content = "content")
        When("댓글과 글쓴이가 실제로 있을 경우 저장한다면") {
            val parentComment = Comment(id = 1L, content = "content", author = buildMember(), review = buildReview())
            every { commentRepository.getReferenceById(any(Long::class)) }.returns(parentComment)
            every { memberRepository.getReferenceById(any(Long::class)) }.returns(buildMember())
            val newComment = commentService.postChildComment(commentId, authorId, commentDto)
            Then("대댓글 데이터가 반환된다.") {
                newComment.content shouldBe commentDto.content
            }
            Then("댓글에 대댓글이 추가된다.") {
                parentComment.getChildComments().size shouldBe 1
            }
            Then("댓글을 조회한다.") {
                verify { commentRepository.getReferenceById(any(Long::class)) }
            }
            Then("글쓴이를 조회한다.") {
                verify { memberRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("댓글이 없을 때 저장한다면") {
            every { commentRepository.getReferenceById(any(Long::class)) }.throws(EntityNotFoundException())
            val exception =
                shouldThrow<CustomException> { commentService.postChildComment(commentId, authorId, commentDto) }
            Then("예외가 발생한다") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("리뷰를 조회한다.") {
                verify { reviewRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("글쓴이가 없을 때 저장한다면") {
            val parentComment = Comment(id = 1L, content = "content", author = buildMember(), review = buildReview())
            every { commentRepository.getReferenceById(any(Long::class)) }.returns(parentComment)
            every { memberRepository.getReferenceById(any(Long::class)) }.throws(EntityNotFoundException())
            val exception = shouldThrow<CustomException> { commentService.postComment(commentId, authorId, commentDto) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("댓글를 조회한다.") {
                verify { commentRepository.getReferenceById(any(Long::class)) }
            }
            Then("글쓴이를 조회한다.") {
                verify { memberRepository.getReferenceById(any(Long::class)) }
            }
        }
    }

    Given("댓글 아이디와 저자아이디, 댓글 정보가 주어졌을 때") {
        val commentId = 1L
        val authorId = 1L
        val commentDto = CommentDto(content = "content")
        When("수정에 성공한다면") {
            every { commentRepository.findByIdOrNull(any(Long::class)) }.returns(
                Comment(
                    id = 1L,
                    content = "content",
                    author = buildMember(),
                    review = buildReview()
                )
            )
            val updatedComment = commentService.updateComment(commentId, authorId, commentDto)
            Then("데이터가 수정된다.") {
                updatedComment.content shouldBe commentDto.content
            }
            Then("댓글을 조회한다.") {
                verify { commentRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("댓글이 없다면") {
            every { commentRepository.findByIdOrNull(any(Long::class)) }.returns(null)
            val exception =
                shouldThrow<CustomException> { commentService.updateComment(commentId, authorId, commentDto) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("댓글을 조회한다.") {
                verify { commentRepository.getReferenceById(any(Long::class)) }
            }
        }
        When("댓글이 접근한 사용자가 쓴 글이 아니라면") {
            every { commentRepository.findByIdOrNull(any(Long::class)) }.returns(
                Comment(
                    id = 1L,
                    content = "content",
                    author = buildMember(),
                    review = buildReview()
                )
            )
            val exception =
                shouldThrow<CustomException> { commentService.updateComment(commentId, 10L, commentDto) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.UNAUTHENTICATED_USER.code
                exception.message shouldBe ResultStatus.UNAUTHENTICATED_USER.message
            }
            Then("댓글을 조회한다.") {
                verify { commentRepository.findByIdOrNull(any(Long::class)) }
            }
        }
    }

    Given("댓글 아이디와 저자아이디가 주어졌을 때") {
        val commentId = 1L
        val authorId = 1L
        When("삭제에 성공한다면") {
            every { commentRepository.findByIdOrNull(any(Long::class)) }.returns(
                Comment(
                    id = 1L,
                    content = "content",
                    author = buildMember(),
                    review = buildReview()
                )
            )
            every { commentRepository.delete(any(Comment::class)) }.returns(Unit)
            commentService.deleteComment(commentId, authorId)
            Then("댓글을 조회한다.") {
                verify { commentRepository.findByIdOrNull(any(Long::class)) }
            }
            Then("삭제를 한다.") {
                verify { commentRepository.delete(any(Comment::class)) }
            }
        }
        When("댓글이 없다면") {
            every { commentRepository.findByIdOrNull(any(Long::class)) }.returns(null)
            val exception =
                shouldThrow<CustomException> { commentService.deleteComment(commentId, authorId) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception.message shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.message
            }
            Then("댓글을 조회한다.") {
                verify { commentRepository.findByIdOrNull(any(Long::class)) }
            }
        }
        When("댓글이 접근한 사용자가 쓴 글이 아니라면") {
            every { commentRepository.findByIdOrNull(any(Long::class)) }.returns(
                Comment(
                    id = 1L,
                    content = "content",
                    author = buildMember(),
                    review = buildReview()
                )
            )
            val exception =
                shouldThrow<CustomException> { commentService.deleteComment(commentId, 10L) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResultStatus.UNAUTHENTICATED_USER.code
                exception.message shouldBe ResultStatus.UNAUTHENTICATED_USER.message
            }
            Then("댓글을 조회한다.") {
                verify { commentRepository.findByIdOrNull(any(Long::class)) }
            }
        }
    }

}) {
    companion object {
        fun buildRestaurant() = Restaurant(
            id = 1L,
            name = "Restaurant",
            address = "Address",
            roadAddressName = "Road Address",
            phoneNumber = "010-1234-5678",
            websiteUrl = "www.naver.com",
            latitude = 0.0,
            longitude = 0.0,
            owner = buildMember()
        )

        fun buildMember() = Member(id = 1L, username = "testUser", password = "1234", email = "test@test.com")
        fun buildReview() =
            Review(id = 1L, restaurant = buildRestaurant(), author = buildMember(), content = "text", title = "title")
    }
}