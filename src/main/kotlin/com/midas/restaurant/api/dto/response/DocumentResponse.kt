package com.midas.restaurant.api.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DocumentResponse(
    @field:JsonProperty("id") val id: String,
    @field:JsonProperty("place_name") val placeName: String,
    @field:JsonProperty("category_name") val categoryName: String,
    @field:JsonProperty("category_group_code") val categoryGroupCode: String,
    @field:JsonProperty("category_group_name") val categoryGroupName: String,
    @field:JsonProperty("phone") val phone: String,
    @field:JsonProperty("address_name") val addressName: String,
    @field:JsonProperty("road_address_name") val roadAddressName: String,
    @field:JsonProperty("y") val latitude: Double,
    @field:JsonProperty("x") val longitude: Double,
    @field:JsonProperty("place_url") val placeUrl: String,
    @field:JsonProperty("distance") val distance: Double
) {
}