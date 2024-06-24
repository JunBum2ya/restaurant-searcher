package com.midas.restaurant.review.repository

import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import com.midas.restaurant.review.domain.Comment
import com.midas.restaurant.review.domain.Review
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles

@DisplayName("리포지토리 테스트 - 댓글")
@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val restaurantRepository: RestaurantRepository,
    @Autowired private val reviewRepository: ReviewRepository,
    @Autowired private val commentRepository: CommentRepository
) {

    private var member = Member(username = "testUser", password = "testPassword", email = "test@test.com")
    private var restaurant = Restaurant(
        owner = member,
        address = "address",
        roadAddressName = "roadAddress",
        name = "name",
        phoneNumber = "010-1234-1234",
        websiteUrl = "www.naver.com",
        latitude = 0.0,
        longitude = 0.0
    )
    private var review =
        Review(id = 1, author = member, restaurant = restaurant, title = "testTitle", content = "testContent")
    private var parentComment = Comment(review = review, author = member, content = "testContent")

    @BeforeEach
    fun initData() {
        member =
            memberRepository.save(Member(username = "testUser", password = "testPassword", email = "test@test.com"))
        restaurant = restaurantRepository.save(
            Restaurant(
                owner = member,
                address = "address",
                roadAddressName = "roadAddress",
                name = "name",
                phoneNumber = "010-1234-1234",
                websiteUrl = "www.naver.com",
                latitude = 0.0,
                longitude = 0.0
            )
        )
        review = reviewRepository.save(
            Review(
                id = 1,
                author = member,
                restaurant = restaurant,
                title = "testTitle",
                content = "testContent"
            )
        )
        parentComment = commentRepository.save(Comment(review = review, author = member, content = "testContent"))
        parentComment.addChildComment(Comment(author = member, content = "testContent"))
        commentRepository.flush()
    }

    @DisplayName("댓글 저장 테스트")
    @Test
    fun givenReview_whenSaveComment_thenAddComment() {
        //given
        val comment = Comment(author = member, content = "testContent")
        //when
        review.addComment(comment)
        reviewRepository.flush()
        //then
        commentRepository.findAll().size shouldBe 3
    }
    
    @DisplayName("대댓글 저장 테스트")
    @Test
    fun givenComment_whenSaveChildComment_thenAddChildComment() {
        //given
        val comment = Comment(author = member, content = "testContent")
        //when
        parentComment.addChildComment(comment)
        commentRepository.flush()
        //then
        commentRepository.findAll().size shouldBe 3
    }
    
    @DisplayName("리뷰를 사용하여 댓글을 조회")
    @Test
    fun givenReview_whenSearchComment_thenReturnsCommentPage() {
        //given
        val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "createdAt"))
        //when
        val page = commentRepository.findCommentsByReview(review, pageable)
        //then
        page.totalPages shouldBe 1
        page.totalElements shouldBe 1
        page.size shouldBe pageable.pageSize
    }

    @DisplayName("댓글을 사용하여 대댓글을 조회")
    @Test
    fun givenComment_whenSearchComment_thenReturnsCommentPage() {
        //given
        val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "createdAt"))
        //when
        val page = commentRepository.findCommentsByParentCommentId(parentComment.getId()!!, pageable)
        //then
        page.totalPages shouldBe 1
        page.totalElements shouldBe 1
        page.size shouldBe pageable.pageSize
    }

}