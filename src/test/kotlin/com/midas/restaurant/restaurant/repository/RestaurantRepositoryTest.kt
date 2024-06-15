package com.midas.restaurant.restaurant.repository

import com.midas.restaurant.config.JpaConfig
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.member.repository.MemberRepository
import com.midas.restaurant.restaurant.domain.Restaurant
import io.kotest.core.spec.style.BehaviorSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@DisplayName("리포지토리 테스트 - 약국")
@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfig::class)
class RestaurantRepositoryTest(
    @Autowired private val restaurantRepository: RestaurantRepository,
    @Autowired private val memberRepository: MemberRepository
) {

    private var owner: Member? = null

    @BeforeEach
    fun saveOwner() {
        this.owner = memberRepository.save(Member(username = "test", password = "test", email = "test@test.com"))
    }

    @DisplayName("음식점 저장 기능")
    @Test
    fun saveRestaurant() {
        //given
        val restaurant = Restaurant(
            address = "경기도",
            name = "토마토",
            roadAddressName = "경기도",
            majorCategory = "일식점",
            minorCategory = "초밥집",
            latitude = 10.0,
            longitude = 10.0,
            websiteUrl = "",
            phoneNumber = "",
            owner = owner!!
        )
        //when
        val savedRestaurant = restaurantRepository.save(restaurant)
        //then
        assertThat(savedRestaurant).isNotNull
        assertThat(savedRestaurant.getId()).isNotNull()
        assertThat(savedRestaurant.name).isEqualTo(restaurant.name)
        assertThat(savedRestaurant.address).isEqualTo(restaurant.address)
        assertThat(savedRestaurant.latitude).isEqualTo(restaurant.latitude)
        assertThat(savedRestaurant.longitude).isEqualTo(restaurant.longitude)
        assertThat(savedRestaurant.getCreatedAt()).isNotNull()
        assertThat(savedRestaurant.getUpdatedAt()).isNotNull()
    }

    @DisplayName("음식점 데이터 리스트를 저장한다.")
    @Test
    fun saveAllRestaurant() {
        //given
        val restaurantList = listOf(
            Restaurant(
                address = "경기도",
                name = "토마토",
                roadAddressName = "경기도",
                majorCategory = "일식점",
                minorCategory = "초밥집",
                latitude = 10.0,
                longitude = 10.0,
                websiteUrl = "",
                phoneNumber = "",
                owner = owner!!
            ),
            Restaurant(
                address = "경기도",
                name = "토마토",
                roadAddressName = "경기도",
                majorCategory = "일식점",
                minorCategory = "초밥집",
                latitude = 10.0,
                longitude = 10.0,
                websiteUrl = "",
                phoneNumber = "",
                owner = owner!!
            )
        )
        //when
        val savedRestaurantList = restaurantRepository.saveAll(restaurantList)
        //then
        assertThat(savedRestaurantList).hasSize(restaurantList.size)
    }

    @DisplayName("음식점 데이터를 조회한다.")
    @Test
    fun searchRestaurant() {
        //given
        val restaurantList = listOf(
            Restaurant(
                address = "경기도",
                name = "토마토",
                roadAddressName = "경기도",
                majorCategory = "일식점",
                minorCategory = "초밥집",
                latitude = 10.0,
                longitude = 10.0,
                websiteUrl = "",
                phoneNumber = "",
                owner = owner!!
            ),
            Restaurant(
                address = "경기도",
                name = "토마토",
                roadAddressName = "경기도",
                majorCategory = "일식점",
                minorCategory = "초밥집",
                latitude = 10.0,
                longitude = 10.0,
                websiteUrl = "",
                phoneNumber = "",
                owner = owner!!
            )
        )
        restaurantRepository.saveAllAndFlush(restaurantList)
        //when
        val page = restaurantRepository.findAll(Pageable.ofSize(10))
        //then
        assertThat(page.number).isEqualTo(0)
        assertThat(page.totalPages).isEqualTo(1)
        assertThat(page.totalElements).isEqualTo(2)
    }

    @DisplayName("음식점 데이터를 삭제한다.")
    @Test
    fun deleteRestaurant() {
        //given
        val restaurantId = restaurantRepository.save(
            Restaurant(
                address = "경기도",
                name = "토마토",
                roadAddressName = "경기도",
                majorCategory = "일식점",
                minorCategory = "초밥집",
                latitude = 10.0,
                longitude = 10.0,
                websiteUrl = "",
                phoneNumber = "",
                owner = owner!!
            )
        ).getId() ?: -1L
        //when
        restaurantRepository.deleteById(restaurantId)
        val deletedRestaurant = restaurantRepository.findByIdOrNull(restaurantId)
        //then
        assertThat(deletedRestaurant).isNull()
    }

}