package com.woowahan.campus.announcement.exception

import org.springdoc.api.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AnnouncementExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ResponseEntity<ErrorMessage> {
        return ResponseEntity.badRequest().body(ErrorMessage(exception.message))
    }

    @ExceptionHandler(AuthorizationException::class)
    fun handleUnauthorizedException(exception: AuthorizationException): ResponseEntity<ErrorMessage> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessage(exception.message))
    }
}
