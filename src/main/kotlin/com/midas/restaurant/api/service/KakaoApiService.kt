package com.midas.restaurant.api.service

import com.midas.restaurant.api.contant.KakaoCategory
import com.midas.restaurant.api.dto.request.KakaoAddressSearchRequest
import com.midas.restaurant.api.dto.request.KakaoCategorySearchRequest
import com.midas.restaurant.api.dto.response.CategoryDocumentResponse
import com.midas.restaurant.api.dto.response.AddressDocumentResponse
import com.midas.restaurant.api.dto.response.KakaoApiResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class KakaoApiService(
    @Value("\${kakao.rest.api.key}") private val kakaoRestApiKey: String,
    private val restTemplate: RestTemplate
) {

    private val logger = LoggerFactory.getLogger(KakaoApiService::class.java)

    fun requestAddressSearch(address: String): KakaoApiResponse<AddressDocumentResponse>? {
        if(address.isEmpty()) {
            return null
        }
        val uri = KakaoAddressSearchRequest(address).toUri()
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK $kakaoRestApiKey")
        val httpEntity = HttpEntity<HttpHeaders>(headers)
        return restTemplate.exchange<KakaoApiResponse<AddressDocumentResponse>>(uri, HttpMethod.GET, httpEntity).body
    }

    /**
     * 카카오 카테고리 검색
     */
    fun requestCategorySearch(
        latitude: Double,
        longitude: Double,
        radius: Double,
        category: KakaoCategory
    ): KakaoApiResponse<CategoryDocumentResponse>? {
        val uri = KakaoCategorySearchRequest(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            category = category
        ).toUri()
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK $kakaoRestApiKey")
        val httpEntity = HttpEntity<HttpHeaders>(headers)
        return restTemplate.exchange<KakaoApiResponse<CategoryDocumentResponse>>(uri, HttpMethod.GET, httpEntity).body
    }

}