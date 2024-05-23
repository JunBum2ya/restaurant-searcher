package com.midas.restaurant.api.dto.request

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

data class KakaoAddressSearchRequest(val address: String) {

    private val KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json"

    fun toUri(): URI {
        return UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL)
            .queryParam("query", address)
            .build()
            .encode()
            .toUri()
    }
}