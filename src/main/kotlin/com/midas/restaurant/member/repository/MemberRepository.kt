package com.midas.restaurant.member.repository;

import com.midas.restaurant.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findMemberByUsername(username: String): Member?
}