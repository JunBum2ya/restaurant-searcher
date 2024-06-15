package com.midas.restaurant.review.repository

import com.midas.restaurant.config.JpaConfig
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import com.midas.restaurant.review.domain.Review
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles


@DisplayName("리포지토리 테스트 - 리뷰")
@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfig::class)
class ReviewRepositoryTest(
    @Autowired private val reviewRepository: ReviewRepository,
    @Autowired private val restaurantRepository: RestaurantRepository,
    @Autowired private val memberRepository: MemberRepository
) {

    private var restaurant: Restaurant? = null
    private var member: Member? = null
    private var review: Review? = null

    @BeforeEach
    fun saveRestaurant() {
        val owner = memberRepository.save(Member(username = "test", password = "1234", email = "test@test.com"))
        val data = Restaurant(
            name = "test",
            address = "test",
            roadAddressName = "test",
            latitude = 0.0,
            longitude = 0.0,
            phoneNumber = "test",
            websiteUrl = "test",
            owner = owner
        )
        this.restaurant = restaurantRepository.save(data)
    }

    @BeforeEach
    fun saveMember() {
        val data = Member(username = "test1", password = "test", email = "test@test.com")
        this.member = memberRepository.save(data)
    }

    @BeforeEach
    fun saveReview() {
        val data = Review(restaurant = restaurant!!, author = member!!, title = "title", content = "content")
        this.review = reviewRepository.save(data)
    }

    @DisplayName("Pageable이 주어졌을 때 리뷰를 조회하면 페이지가 반환된다")
    @Test
    fun givenPageable_whenSearchReviews_thenReturnsReviewPage() {
        //given
        val pageable = PageRequest.of(0, 10)
        //when
        val page = reviewRepository.findAll(pageable)
        //then
        assertThat(page).isNotEmpty
        assertThat(page.size).isEqualTo(10)
        assertThat(page.number).isEqualTo(0)
        assertThat(page.totalElements).isEqualTo(1)
    }

    @DisplayName("리뷰 아이디가 주어졌을 때 리뷰를 조회한다면 리뷰가 반환된다.")
    @Test
    fun givenReviewId_whenSearchReview_thenReturnsReview() {
        //given
        val reviewId = this.review?.id!!
        //when
        val findReview = reviewRepository.getReferenceById(reviewId)
        //then
        assertThat(findReview).isNotNull
        assertThat(findReview.id).isEqualTo(reviewId)
    }

    @DisplayName("리뷰 데이터가 주어졌을 때 리뷰를 저장하면 리뷰가 반환된다.")
    @Test
    fun givenReview_whenSaveReview_thenReturnsReview() {
        //given
        val review = Review(restaurant = restaurant!!, author = member!!, title = "new title", content = "new content")
        //when
        val savedReview = reviewRepository.save(review)
        //then
        assertThat(savedReview).isNotNull
        assertThat(savedReview.id).isNotNull()
        assertThat(savedReview.getCreatedAt()).isNotNull()
        assertThat(savedReview.getUpdatedAt()).isNotNull()
    }

    @DisplayName("제목과 내용을 수정하면 실제 DB가 수정된다.")
    @Test
    fun givenNewTitleAndNewContent_whenUpdateReview_thenUpdateReview() {
        //given
        val title = "new title"
        val content = "new content"
        //when
        review?.update(title = title, content = content)
        reviewRepository.flush()
        //then
        val updatedReview = reviewRepository.findById(review?.id!!).get()
        assertThat(updatedReview.getTitle()).isEqualTo(title)
        assertThat(updatedReview.getContent()).isEqualTo(content)
    }

    @DisplayName("리뷰를 삭제하면 실제 DB에서 데이터가 삭제된다.")
    @Test
    fun givenReview_whenDeleteReview_thenDeleteReview() {
        //given
        //when
        reviewRepository.delete(review!!)
        reviewRepository.flush()
        //then
        val list = reviewRepository.findAll()
        assertThat(list).isEmpty()
    }

}