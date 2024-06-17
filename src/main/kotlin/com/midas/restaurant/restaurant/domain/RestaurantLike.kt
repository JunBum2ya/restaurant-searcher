package com.midas.restaurant.restaurant.domain

import com.midas.restaurant.common.domain.BaseEntity
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.review.domain.Review
import jakarta.persistence.*

@Entity
class RestaurantLike(
    @Id @GeneratedValue private var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "restaurant_id") val restaurant: Restaurant,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "member_id") val member: Member,
    var stars: Float
) : BaseEntity() {

    fun update(stars: Float?): RestaurantLike {
        stars?.let { this.stars = it }
        return this
    }

}