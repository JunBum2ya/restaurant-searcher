package com.midas.restaurant.direction.repository

import com.midas.restaurant.config.JpaConfig
import com.midas.restaurant.direction.domain.Direction
import com.midas.restaurant.restaurant.domain.Restaurant
import com.midas.restaurant.restaurant.repository.RestaurantRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles

@DisplayName("리포지토리 테스트 - 방향")
@DataJpaTest
@Import(JpaConfig::class)
@ActiveProfiles("test")
class DirectionRepositoryTest(
    @Autowired private val directionRepository: DirectionRepository,
    @Autowired private val restaurantRepository: RestaurantRepository
) {

    @DisplayName("Direction을 저장하면 Direction이 반환된다.")
    @Test
    fun saveDirection() {
        val restaurant = createRestaurant()
        val direction = directionRepository.save(
            Direction(
                distance = 4.54,
                inputAddress = "경기도",
                inputLatitude = 4.45,
                inputLongitude = 43.34,
                restaurant = restaurant
            )
        )
        assertThat(direction).isNotNull
        assertThat(direction.getId()).isNotNull()
    }

    @DisplayName("Direction 조회")
    @Test
    fun findDirection() {
        directionRepository.save(
            Direction(
                distance = 4.54,
                inputAddress = "경기도",
                inputLatitude = 4.45,
                inputLongitude = 43.34,
                restaurant = createRestaurant()
            )
        )
        val pageable = PageRequest.of(0, 10)
        //when
        val page = directionRepository.findAll(pageable)
        //then
        assertThat(page.size).isEqualTo(10)
        assertThat(page.totalElements).isEqualTo(1)
        assertThat(page.number).isEqualTo(0)
    }

    fun deleteDirection() {
        val direction = directionRepository.saveAndFlush(
            Direction(
                distance = 4.54,
                inputAddress = "경기도",
                inputLatitude = 4.45,
                inputLongitude = 43.34,
                restaurant = createRestaurant()
            )
        )
        //when
        directionRepository.deleteById(direction.getId() ?: -1)
        //then
        val list = directionRepository.findAll()
        assertThat(list).isEmpty()
    }

    private fun createRestaurant() = restaurantRepository.save(
        Restaurant(
            address = "경기도",
            name = "토마토",
            roadAddressName = "경기도",
            majorCategory = "일식점",
            minorCategory = "초밥집",
            latitude = 10.0,
            longitude = 10.0,
            websiteUrl = "",
            phoneNumber = ""
        )
    )

}