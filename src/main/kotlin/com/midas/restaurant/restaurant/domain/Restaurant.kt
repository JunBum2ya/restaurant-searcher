package com.midas.restaurant.restaurant.domain

import com.midas.restaurant.common.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Restaurant(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    val name: String,
    val address: String,
    val roadAddressName: String,
    val majorCategory: String,
    val minorCategory: String,
    val phoneNumber: String,
    val websiteUrl: String,
    val latitude: Double,
    val longitude: Double
): BaseEntity() {

    fun getId(): Long? {
        return id
    }

}