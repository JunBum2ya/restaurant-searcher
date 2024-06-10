package com.midas.restaurant.member.domain

import com.midas.restaurant.common.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private val id: Long? = null,
    private var username: String,
    private var password: String,
    private var email: String,
): BaseEntity() {
}