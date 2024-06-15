package com.midas.restaurant.common.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.restaurant.common.util.DateUtil
import com.midas.restaurant.member.dto.MemberDetails
import com.midas.restaurant.member.dto.MemberDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.time.LocalDateTime
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.access-token-validity-in-seconds}") tokenValidityInSeconds: Long,
    private val objectMapper: ObjectMapper
) : InitializingBean {

    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    private val AUTHORITIES_KEY = "auth"
    private val tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
    private var key: Key? = null

    override fun afterPropertiesSet() {
        val keyBytes = Base64.getDecoder().decode(secret.toByteArray())
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(member: MemberDto): String {
        val details = buildMemberDetails(member)
        return Jwts.builder()
            .setSubject(details.username)
            .claim(AUTHORITIES_KEY, details.authorities)
            .claim("details", details.serialize())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(DateUtil.convertLocalDateTimeToDate(details.expiredDate))
            .compact()
    }

    fun extractAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val details = MemberDetails.deserialize(claims["details"].toString())
        return UsernamePasswordAuthenticationToken(details, details.password, details.authorities)
    }

    /**
     * principal 생성
     */
    private fun buildMemberDetails(member: MemberDto): MemberDetails {
        return MemberDetails.from(
            memberDto = member,
            expiredTime = tokenValidityInMilliseconds,
            authorities = mutableListOf(SimpleGrantedAuthority("GUEST"))
        )
    }

}