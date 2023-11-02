package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.AnnouncementRepository
import jakarta.transaction.Transactional
import openapi.api.GetAnnouncementApi
import openapi.model.AnnouncementResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional
class GetAnnouncement(
    val announcementRepository: AnnouncementRepository
) : GetAnnouncementApi {

    override fun findAnnouncementById(id: Int, authorization: String): ResponseEntity<AnnouncementResponse> {
        val announcement = announcementRepository.findById(id.toLong())
        return ResponseEntity.ok().body(toResponse(announcement.get()));
    }

    private fun toResponse(announcement: Announcement): AnnouncementResponse {
        return AnnouncementResponse(
            announcement.id.toInt(),
            announcement.title,
            announcement.content,
            announcement.author,
            announcement.createdAt.toString(),
        )
    }
}
