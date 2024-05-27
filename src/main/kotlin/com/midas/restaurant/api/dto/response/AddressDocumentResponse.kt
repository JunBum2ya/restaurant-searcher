package com.midas.restaurant.api.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

class AddressDocumentResponse(
    @field:JsonProperty("address_name") val name: String,
    @field:JsonProperty("y") val latitude: Double,
    @field:JsonProperty("x") val longitude: Double,
) : DocumentResponse {
}