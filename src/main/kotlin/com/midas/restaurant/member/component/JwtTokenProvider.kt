package com.midas.restaurant.member.component

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
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.access-token-validity-in-seconds}") tokenValidityInSeconds: Long,
) : InitializingBean {

    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    private val AUTHORITIES_KEY = "auth"
    val tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
    private var key: Key? = null

    override fun afterPropertiesSet() {
        val keyBytes = Base64.getDecoder().decode(secret.toByteArray())
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(details: MemberDetails): String {
        return Jwts.builder()
            .setSubject(details.username)
            .claim(AUTHORITIES_KEY, details.authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(Date(Date().time + tokenValidityInMilliseconds))
            .compact()
    }

}