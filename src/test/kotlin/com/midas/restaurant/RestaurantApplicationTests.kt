package com.midas.restaurant

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(properties = ["kakao.rest.api.key=your-test-api-key"])
@ActiveProfiles("test")
class RestaurantApplicationTests {

    @Test
    fun contextLoads() {
    }

}
