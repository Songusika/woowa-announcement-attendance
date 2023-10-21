package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.AnnouncementRepository
import openapi.api.CreateAnnouncementApi
import openapi.model.CreateAnnouncementRequest
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CreateAnnouncement(
    val announcementRepository: AnnouncementRepository
) : CreateAnnouncementApi {

    @Transactional
    override fun createAnnouncement(
        authorization: String,
        createAnnouncementRequest: CreateAnnouncementRequest
    ): ResponseEntity<Unit> {

        val savedAnnouncement = announcementRepository.save(
            Announcement(
                createAnnouncementRequest.title,
                createAnnouncementRequest.content,
                createAnnouncementRequest.author,
                createAnnouncementRequest.slackChannel.channelId
            )
        )
        return ResponseEntity.created(URI.create("/api/announcements/${savedAnnouncement.id}")).build()
    }
}
