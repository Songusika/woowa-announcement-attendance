package com.woowahan.campus.announcement.exception

import org.springdoc.api.ErrorMessage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AnnouncementExceptionHandler {

    @ExceptionHandler
    fun handleAnnouncementNotFoundException(exception: IllegalArgumentException): ResponseEntity<ErrorMessage> {
        return ResponseEntity.badRequest().body(ErrorMessage(exception.message))
    }

}
