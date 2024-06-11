package com.midas.restaurant.member.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.member.component.JwtTokenProvider
import com.midas.restaurant.member.dto.AuthToken
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.member.repository.MemberRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun join(memberDto: MemberDto): MemberDto {
        val existMember = memberRepository.findMemberByUsername(memberDto.username)
        if (existMember != null) {
            throw CustomException(ResultStatus.DUPLICATE_UNIQUE_PROPERTY, "중복된 사용자가 있습니다.")
        }
        val registeredMember = memberRepository.save(memberDto.toEntity(passwordEncoder))
        return MemberDto(registeredMember)
    }

    @Transactional(readOnly = true)
    fun login(username: String, password: String): AuthToken {
        val member = memberRepository.findMemberByUsername(username)
            ?: throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
        }
        val details = MemberDetails.from(
            MemberDto(member),
            jwtTokenProvider.tokenValidityInMilliseconds,
            mutableListOf(SimpleGrantedAuthority("GUEST"))
        )
        val authToken = AuthToken(username = member.getUsername(), token = jwtTokenProvider.generateToken(details))
        return authToken
    }

}