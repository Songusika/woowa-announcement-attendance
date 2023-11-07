package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.AnnouncementRepository
import com.woowahan.campus.announcement.domain.Author
import com.woowahan.campus.announcement.domain.Content
import com.woowahan.campus.announcement.domain.Title
import com.woowahan.campus.announcement.domain.getById
import openapi.api.UpdateAnnouncementApi
import openapi.model.UpdateAnnouncementRequest
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateAnnouncement(
    val announcementRepository: AnnouncementRepository
) : UpdateAnnouncementApi {

    @Transactional
    override fun updateAnnouncement(
        id: Long,
        authorization: String,
        updateAnnouncementRequest: UpdateAnnouncementRequest
    ): ResponseEntity<Unit> {
        val announcement = announcementRepository.getById(id)

        announcement.update(
            Title(updateAnnouncementRequest.title),
            Content(updateAnnouncementRequest.content),
            Author(updateAnnouncementRequest.author)
        )

        announcementRepository.save(announcement)

        return ResponseEntity.ok().build()
    }
}
