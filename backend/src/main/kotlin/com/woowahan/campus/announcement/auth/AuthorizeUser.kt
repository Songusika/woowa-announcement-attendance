package com.woowahan.campus.announcement.auth

import com.woowahan.campus.announcement.exception.AuthorizationException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*

class AuthorizeUser(
    val password: String
): HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        val basicHeader = authorizationHeader.substringAfter("Basic ")
        val password = String(Base64.getDecoder().decode(basicHeader))
        if (this.password != password) {
            throw AuthorizationException("인증 오류")
        }
        return true
    }
}