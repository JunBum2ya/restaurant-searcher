package com.midas.restaurant.member.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.common.component.JwtTokenProvider
import com.midas.restaurant.member.domain.cache.MemberCache
import com.midas.restaurant.member.dto.AuthToken
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.member.dto.MemberDto
import com.midas.restaurant.member.repository.MemberRedisRepository
import com.midas.restaurant.member.repository.MemberRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRedisRepository: MemberRedisRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun join(memberDto: MemberDto): AuthToken {
        if (findMemberByUsername(memberDto.username) != null) {
            throw CustomException(ResultStatus.DUPLICATE_UNIQUE_PROPERTY, "중복된 사용자가 있습니다.")
        }
        val registeredMember = memberRepository.save(memberDto.toEntity(passwordEncoder))
        memberRedisRepository.save(MemberCache.from(registeredMember))
        return AuthToken(
            username = registeredMember.getUsername(),
            token = jwtTokenProvider.generateToken(MemberDto.from(registeredMember))
        )
    }

    @Transactional(readOnly = true)
    fun login(username: String, password: String): AuthToken {
        val member = findMemberByUsername(username) ?: throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
        if (!passwordEncoder.matches(password, member.password)) {
            throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
        }
        return AuthToken(username = member.username, token = jwtTokenProvider.generateToken(member))
    }

    @Transactional(readOnly = true)
    fun findMemberByUsername(username: String): MemberDto? {
        return memberRedisRepository.findByIdOrNull(username)?.let { MemberDto.from(it) }
            ?: memberRepository.findMemberByUsername(username)?.let {
                MemberDto.from(memberRedisRepository.save(MemberCache.from(it)))
            }
    }

}