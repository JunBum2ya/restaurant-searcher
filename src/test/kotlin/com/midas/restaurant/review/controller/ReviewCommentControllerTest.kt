package com.midas.restaurant.review.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.config.TestSecurityConfig
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.review.dto.CommentDto
import com.midas.restaurant.review.dto.request.CommentRequest
import com.midas.restaurant.review.service.CommentService
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.print.print
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.hasSize
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [ReviewCommentController::class])
@Import(TestSecurityConfig::class)
class ReviewCommentControllerTest(
    @Autowired val mvc: MockMvc,
    @Autowired objectMapper: ObjectMapper,
    @MockkBean val commentService: CommentService
) :
    DescribeSpec({

        extensions(SpringExtension)

        describe("리뷰 댓글 조회") {
            val reviewId = 1L
            context("조회 성공") {
                every { commentService.searchComments(any(Long::class), any(Pageable::class)) }.returns(Page.empty())
                it("200 OK") {
                    mvc.perform(get("/api/v1/review/$reviewId/comment"))
                        .andExpect(status().isOk)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                        .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                        .andExpect(jsonPath("data.content").isEmpty)
                    verify { commentService.searchComments(any(Long::class), any(Pageable::class)) }
                }
            }
            context("조회 실패") {
                every { commentService.searchComments(any(Long::class), any(Pageable::class)) }
                    .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
                it("4XX ERROR") {
                    mvc.perform(get("/api/v1/review/$reviewId/comment"))
                        .andExpect(status().isBadRequest)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("code").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.code))
                        .andExpect(jsonPath("message").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.message))
                    verify { commentService.searchComments(any(Long::class), any(Pageable::class)) }
                }
            }
        }

        describe("대댓글 조회") {
            val reviewId = 1L
            val commentId = 1L
            context("조회 성공") {
                every { commentService.searchChildComments(any(Long::class), any(Long::class), any(Pageable::class)) }
                    .returns(Page.empty())
                it("200 OK") {
                    mvc.perform(get("/api/v1/review/$reviewId/comment/$commentId"))
                        .andExpect(status().isOk)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                        .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                        .andExpect(jsonPath("data.content").isEmpty)
                    verify {
                        commentService.searchChildComments(
                            any(Long::class),
                            any(Long::class),
                            any(Pageable::class)
                        )
                    }
                }
            }
            context("조회 실패") {
                every { commentService.searchChildComments(any(Long::class), any(Long::class), any(Pageable::class)) }
                    .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
                it("4XX ERROR") {
                    mvc.perform(get("/api/v1/review/$reviewId/comment/$commentId"))
                        .andExpect(status().isBadRequest)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("code").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.code))
                        .andExpect(jsonPath("message").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.message))
                    verify {
                        commentService.searchChildComments(
                            any(Long::class),
                            any(Long::class),
                            any(Pageable::class)
                        )
                    }
                }
            }
        }

        describe("리뷰 댓글 작성") {
            val reviewId = 1L
            val request = CommentRequest(content = "test")
            context("댓글 작성 성공") {
                every { commentService.postComment(any(Long::class), any(Long::class), any(CommentDto::class)) }
                    .returns(CommentDto(id = 1L, content = request.content!!))
                it("200 OK") {
                    mvc.perform(post("/api/v1/review/$reviewId/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                        .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                        .andExpect(jsonPath("data.content").value(request.content))
                    verify { commentService.postComment(any(Long::class), any(Long::class), any(CommentDto::class)) }
                }
            }
            context("댓글 작성 실패") {
                every { commentService.postComment(any(Long::class), any(Long::class), any(CommentDto::class)) }
                    .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
                it("4XX ERROR") {
                    mvc.perform(post("/api/v1/review/$reviewId/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("code").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.code))
                        .andExpect(jsonPath("message").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.message))
                    verify { commentService.postComment(any(Long::class), any(Long::class), any(CommentDto::class)) }
                }
            }
            context("일부 파라미터 누락 시") {
                it("400 ERROR") {
                    mvc.perform(post("/api/v1/review/$reviewId/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CommentRequest(content = null))))
                        .andExpect(status().isBadRequest)
                        .andExpect(jsonPath("code").value(ResultStatus.NOT_VALID_REQUEST.code))
                        .andExpect(jsonPath("message").exists())
                        .andExpect(jsonPath("data").isArray)
                }
            }
        }

        describe("대댓글 작성") {
            val reviewId = 1L
            val commentId = 1L
            val request = CommentRequest(content = "test")
            context("대댓글 작성 성공") {
                every { commentService.postChildComment(any(Long::class), any(Long::class), any(CommentDto::class)) }
                    .returns(CommentDto(id = 1L, content = request.content!!))
                it("200 OK") {
                    mvc.perform(post("/api/v1/review/$reviewId/comment/$commentId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("code").value(ResultStatus.SUCCESS.code))
                        .andExpect(jsonPath("message").value(ResultStatus.SUCCESS.message))
                        .andExpect(jsonPath("data.content").value(request.content))
                    verify { commentService.postChildComment(any(Long::class), any(Long::class), any(CommentDto::class)) }
                }
            }
            context("대댓글 작성 실패") {
                every { commentService.postChildComment(any(Long::class), any(Long::class), any(CommentDto::class)) }
                    .throws(CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY))
                it("4XX ERROR") {
                    mvc.perform(post("/api/v1/review/$reviewId/comment/$commentId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("code").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.code))
                        .andExpect(jsonPath("message").value(ResultStatus.ACCESS_NOT_EXIST_ENTITY.message))
                    verify { commentService.postChildComment(any(Long::class), any(Long::class), any(CommentDto::class)) }
                }
            }
            context("일부 파라미터 누락 시") {
                it("400 ERROR") {
                    mvc.perform(post("/api/v1/review/$reviewId/comment/$commentId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CommentRequest(content = null))))
                        .andExpect(status().isBadRequest)
                        .andExpect(jsonPath("code").value(ResultStatus.NOT_VALID_REQUEST.code))
                        .andExpect(jsonPath("message").exists())
                        .andExpect(jsonPath("data").isArray)
                }
            }
        }

    })