package com.midas.restaurant.member.domain

import com.midas.restaurant.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private var id: Long? = null,
    @Column(unique = true) private var username: String,
    private var password: String,
    private var email: String,
): BaseEntity() {

    fun getId(): Long? {
        return id
    }

    fun getUsername(): String{
        return username
    }

    fun getPassword(): String{
        return password
    }

    fun getEmail(): String{
        return email
    }

}