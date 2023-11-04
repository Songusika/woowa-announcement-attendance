package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.AnnouncementRepository
import openapi.api.GetAnnouncementApi
import openapi.model.AnnouncementResponse
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional(readOnly = true)
class GetAnnouncement(
    val announcementRepository: AnnouncementRepository
) : GetAnnouncementApi {

    override fun findAnnouncementById(id: Long, authorization: String): ResponseEntity<AnnouncementResponse> {
        val announcement = announcementRepository.findById(id)
            ?: throw IllegalArgumentException("존재하지 않는 announcement입니다.")
        return ResponseEntity.ok().body(toResponse(announcement))
    }

    private fun toResponse(announcement: Announcement): AnnouncementResponse {
        return AnnouncementResponse(
            announcement.id.toInt(),
            announcement.title.title,
            announcement.content.content,
            announcement.author.author,
            announcement.createdAt.toString(),
        )
    }
}
