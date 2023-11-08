package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.AnnouncementRepository
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannelRepository
import com.woowahan.campus.announcement.domain.getById
import openapi.api.GetAnnouncementApi
import openapi.model.AnnouncementResponse
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional(readOnly = true)
class GetAnnouncement(
    val announcementRepository: AnnouncementRepository,
    val announcementSlackChannelRepository: AnnouncementSlackChannelRepository,
) : GetAnnouncementApi {

    override fun findAnnouncementById(id: Long, authorization: String): ResponseEntity<AnnouncementResponse> {
        val announcement = announcementRepository.getById(id)
        return ResponseEntity.ok().body(toResponse(announcement))
    }

    private fun toResponse(announcement: Announcement): AnnouncementResponse {
        return AnnouncementResponse(
            announcement.id,
            announcement.title.title,
            announcement.content.content,
            announcement.author.author,
            announcement.createdAt.toString(),
            announcementSlackChannelRepository.getById(announcement.slackChannelId).name,
        )
    }
}
