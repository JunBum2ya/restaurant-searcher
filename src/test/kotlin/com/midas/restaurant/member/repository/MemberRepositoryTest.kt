package com.midas.restaurant.member.repository

import com.midas.restaurant.config.JpaConfig
import com.midas.restaurant.member.domain.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@DisplayName("리포지토리 테스트 - member")
@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfig::class)
class MemberRepositoryTest(@Autowired private val memberRepository: MemberRepository) {

    @BeforeEach
    fun saveBasicMember() {
        val member = Member(username = "username", password = "password", email = "email@midas.com")
        memberRepository.save(member)
    }

    @DisplayName("username이 주어졌을 때 member를 조회하면 member가 반환된다.")
    @Test
    fun givenUsername_whenFindMember_thenNullableMember() {
        //given
        val username = "username"
        //when
        val member = memberRepository.findMemberByUsername(username)
        //then
        assertThat(member).isNotNull
        assertThat(member?.getUsername()).isEqualTo("username")
        assertThat(member?.getPassword()).isEqualTo("password")
        assertThat(member?.getEmail()).isEqualTo("email@midas.com")
    }

    @DisplayName("member를 저장하면 member가 반환된다.")
    @Test
    fun givenMember_whenSaveMember_thenReturnsMember() {
        //given
        val member = Member(username = "new", password = "password", email = "email@midas.com")
        //when
        val savedMember = memberRepository.save(member)
        //then
        assertThat(savedMember).isNotNull
        assertThat(savedMember.getId()).isNotNull
        assertThat(savedMember.getUsername()).isEqualTo(member.getUsername())
        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword())
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail())
        assertThat(savedMember.getCreatedAt()).isNotNull()
        assertThat(savedMember.getUpdatedAt()).isNotNull()
    }

}