package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.AnnouncementRepository
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
        val announcement = announcementRepository.findById(id)
            ?: throw NoSuchElementException("수정하려는 공지가 존재하지 않습니다.")

        announcement.update(
            updateAnnouncementRequest.title,
            updateAnnouncementRequest.content,
            updateAnnouncementRequest.author
        )

        // TODO: 슬랙 API 호출

        return ResponseEntity.ok().build()
    }
}
