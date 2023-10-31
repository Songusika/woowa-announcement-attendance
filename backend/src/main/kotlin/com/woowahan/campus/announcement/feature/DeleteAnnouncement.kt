package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.AnnouncementRepository
import openapi.api.DeleteAnnouncementApi
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteAnnouncement(
    val announcementRepository: AnnouncementRepository
) : DeleteAnnouncementApi {

    @Transactional
    override fun deleteAnnouncement(id: Long, authorization: String): ResponseEntity<Unit> {
        announcementRepository.deleteById(id)

        return ResponseEntity.noContent().build()
    }
}
