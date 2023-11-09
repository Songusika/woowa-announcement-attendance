package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.AnnouncementRepository
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannelRepository
import com.woowahan.campus.announcement.domain.Author
import com.woowahan.campus.announcement.domain.Content
import com.woowahan.campus.announcement.domain.Title
import com.woowahan.campus.announcement.domain.getByName
import openapi.api.CreateAnnouncementApi
import openapi.model.CreateAnnouncementRequest
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CreateAnnouncement(
    val announcementRepository: AnnouncementRepository,
    val announcementSlackChannelRepository: AnnouncementSlackChannelRepository,
) : CreateAnnouncementApi {

    @Transactional
    override fun createAnnouncement(
        authorization: String,
        createAnnouncementRequest: CreateAnnouncementRequest,
    ): ResponseEntity<Unit> {
        val slackChannel = announcementSlackChannelRepository.getByName(createAnnouncementRequest.slackChannel)

        val savedAnnouncement = announcementRepository.save(
            Announcement.create(
                Title(createAnnouncementRequest.title),
                Content(createAnnouncementRequest.content),
                Author(createAnnouncementRequest.author),
                slackChannel.id,
            )
        )
        return ResponseEntity.created(URI.create("/api/announcements/${savedAnnouncement.id}")).build()
    }
}
