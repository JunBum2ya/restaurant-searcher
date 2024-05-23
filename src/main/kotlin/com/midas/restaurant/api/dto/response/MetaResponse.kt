package com.midas.restaurant.api.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MetaResponse(
    @field:JsonProperty("total_count") val totalCount: Int,
    @field:JsonProperty("pageable_count") val pageableCount: Int,
    @field:JsonProperty("is_end") val end: Boolean
)