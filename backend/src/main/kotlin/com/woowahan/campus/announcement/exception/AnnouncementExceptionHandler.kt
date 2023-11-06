package com.woowahan.campus.announcement.exception

import org.springdoc.api.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AnnouncementExceptionHandler {

    @ExceptionHandler
    fun handleAnnouncementNotFoundException(exception: AnnouncementNotFoundException): ResponseEntity<ErrorMessage> {
        return ResponseEntity.badRequest().body(ErrorMessage(exception.message))
    }

    @ExceptionHandler
    fun handleAuthorizationException(exception: AuthorizationException): ResponseEntity<ErrorMessage> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessage(exception.message))
    }
}
