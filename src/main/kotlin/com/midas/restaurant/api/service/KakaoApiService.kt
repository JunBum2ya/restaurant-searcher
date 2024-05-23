package com.midas.restaurant.api.service

import com.midas.restaurant.api.contant.KakaoCategory
import com.midas.restaurant.api.dto.request.KakaoCategorySearchRequest
import com.midas.restaurant.api.dto.response.KakaApiResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class KakaoApiService(
    @Value("\${kakao.rest.api.key}") private val kakaoRestApiKey: String,
    private val restTemplate: RestTemplate
) {

    private val logger = LoggerFactory.getLogger(KakaoApiService::class.java)

    /**
     * 카카오 카테고리 검색
     */
    fun requestCategorySearch(
        latitude: Double,
        longitude: Double,
        radius: Double,
        category: KakaoCategory
    ): KakaApiResponse? {
        val uri = KakaoCategorySearchRequest(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            category = category
        ).toUri()
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK $kakaoRestApiKey")
        val httpEntity = HttpEntity<HttpHeaders>(headers)
        return restTemplate.exchange<KakaApiResponse>(uri, HttpMethod.GET, httpEntity).body
    }

}