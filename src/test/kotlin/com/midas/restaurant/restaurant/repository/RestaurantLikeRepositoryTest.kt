package com.midas.restaurant.restaurant.repository

import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.domain.RestaurantLike
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DisplayName("리포지토리 테스트 - 별점")
@DataJpaTest
@ActiveProfiles("test")
class RestaurantLikeRepositoryTest(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val restaurantRepository: RestaurantRepository,
    @Autowired private val restaurantLikeRepository: RestaurantLikeRepository
) {

    private var member: Member? = null
    private var restaurant: Restaurant? = null
    private var restaurantLike: RestaurantLike? = null

    @BeforeEach
    fun initData() {
        member = memberRepository.save(Member(username = "test", password = "test", email = "test@test.com"))
        restaurant = restaurantRepository.save(
            Restaurant(
                owner = member!!,
                address = "test",
                roadAddressName = "test",
                name = "test",
                phoneNumber = "test",
                websiteUrl = "test",
                latitude = 0.0,
                longitude = 0.0
            )
        )
        restaurantLike =
            restaurantLikeRepository.save(RestaurantLike(restaurant = restaurant!!, member = member!!, stars = 4.4f))
    }

    @DisplayName("별점 저장")
    @Test
    fun givenMemberAndRestaurant_whenSaveLike_thenReturnsLike() {
        //given
        //when
        val savedLike =
            restaurantLikeRepository.save(RestaurantLike(restaurant = restaurant!!, member = member!!, stars = 4.4f))
        //then
        assertThat(savedLike).isNotNull
        assertThat(savedLike.getId()).isNotNull()
    }

    @DisplayName("별점을 삭제 하면 DB에서 제거된다.")
    @Test
    fun givenLike_whenDeleteLike_thenEmptyLikeAll() {
        //given
        //when
        restaurantLikeRepository.delete(restaurantLike!!)
        //then
        assertThat(restaurantLikeRepository.count()).isZero
    }

    @DisplayName("멤버와 음식점으로 별점을 조회하면 별점을 반환받는다.")
    @Test
    fun givenMemberAndRestaurant_whenSearchLike_thenReturnsLike() {
        //given
        //when
        val like = restaurantLikeRepository.findRestaurantLikeByRestaurantAndMember(restaurant!!, member!!)
        //then
        assertThat(like).isNotNull
    }

}