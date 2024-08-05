package com.midas.restaurant

import org.junit.jupiter.api.Test
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(properties = ["kakao.rest.api.key=your-test-api-key", "jwt.secret=secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey", "jwt.access-token-validity-in-seconds=1800"])
@ActiveProfiles("test")
class RestaurantApplicationTests {

    @MockBean
    lateinit var initializeRedis: CommandLineRunner

    @Test
    fun contextLoads() {
    }

}
