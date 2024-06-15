package com.midas.restaurant.restaurant.domain

import com.midas.restaurant.common.domain.BaseEntity
import com.midas.restaurant.member.domain.Member
import jakarta.persistence.*

@Entity
class Restaurant(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "owner_id") val owner: Member,
    val name: String,
    val address: String,
    val roadAddressName: String,
    val majorCategory: String? = null,
    val minorCategory: String? = null,
    val phoneNumber: String,
    val websiteUrl: String,
    val latitude: Double,
    val longitude: Double
): BaseEntity() {

    fun getId(): Long? {
        return id
    }

}