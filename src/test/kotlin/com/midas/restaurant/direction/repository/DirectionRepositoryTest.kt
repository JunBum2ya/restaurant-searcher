package com.midas.restaurant.direction.repository

import com.midas.restaurant.config.JpaConfig
import com.midas.restaurant.direction.domain.Direction
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
class DirectionRepositoryTest(@Autowired val directionRepository: DirectionRepository) {

    @DisplayName("Direction을 저장하면 Direction이 반환된다.")
    @Test
    fun saveDirection() {
        val direction = directionRepository.save(
            Direction(
                distance = 4.54,
                inputAddress = "경기도",
                targetAddress = "경상도",
                inputLatitude = 4.45,
                inputLongitude = 43.34,
                targetLatitude = 4.45,
                targetLongitude = 43.34,
                targetName = "테스트"
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
                targetAddress = "경상도",
                inputLatitude = 4.45,
                inputLongitude = 43.34,
                targetLatitude = 4.45,
                targetLongitude = 43.34,
                targetName = "테스트"
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

    fun deleteDirection(){
        val direction = directionRepository.saveAndFlush(
            Direction(
                distance = 4.54,
                inputAddress = "경기도",
                targetAddress = "경상도",
                inputLatitude = 4.45,
                inputLongitude = 43.34,
                targetLatitude = 4.45,
                targetLongitude = 43.34,
                targetName = "테스트"
            )
        )
        //when
        directionRepository.deleteById(direction.getId()?:-1)
        //then
        val list = directionRepository.findAll()
        assertThat(list).isEmpty()
    }

}