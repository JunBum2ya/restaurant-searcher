package com.midas.restaurant.api.dto.response

data class KakaoApiResponse<T>(val meta: MetaResponse, val documents: List<T>)
