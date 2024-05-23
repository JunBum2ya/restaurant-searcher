package com.midas.restaurant.api.dto.request

import com.midas.restaurant.api.contant.KakaoCategory
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

data class KakaoCategorySearchRequest(
    val latitude: Double,
    val longitude: Double,
    val radius: Double,
    val page: Int = 1,
    val size: Int = 15,
    val category: KakaoCategory
) {

    private val KAKAO_LOCAL_CATEGORY_SEARCH_URL: String = "https://dapi.kakao.com/v2/local/search/category.json"

    fun toUri(): URI {
        val meterRadius = radius * 1000
        val uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_CATEGORY_SEARCH_URL)
        return uriComponentsBuilder
            .queryParam("category_group_code", category.code)
            .queryParam("x", longitude)
            .queryParam("y", latitude)
            .queryParam("radius", meterRadius)
            .queryParam("page", page)
            .queryParam("size", size)
            .queryParam("sort", "distance")
            .build()
            .encode()
            .toUri()
    }

}