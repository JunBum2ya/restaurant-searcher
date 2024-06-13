package com.midas.restaurant.common

import com.midas.restaurant.common.util.DateUtil
import com.midas.restaurant.member.dto.MemberDetails
import org.springframework.core.MethodParameter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class TestAuthenticationPrincipal : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType.isAssignableFrom(MemberDetails::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        return MemberDetails(
            id = 1L,
            username = "testuser",
            password = "12345",
            email = "test@midas.com",
            authorities = mutableListOf(SimpleGrantedAuthority("TEST_USER")),
            expiredDate = DateUtil.now().plusDays(1)
        )
    }
}