package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.AnnouncementRepository
import com.woowahan.campus.announcement.exception.AnnouncementNotFoundException
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
        if (!announcementRepository.existsById(id)) {
            throw AnnouncementNotFoundException("존재하지 않는 announcement입니다.")
        }

        announcementRepository.deleteById(id)

        return ResponseEntity.noContent().build()
    }
}
