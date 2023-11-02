package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.AnnouncementRepository
import openapi.api.GetAllAnnouncementApi
import openapi.model.AnnouncementInfoResponse
import openapi.model.AnnouncementsInfoByCursorResponse
import openapi.model.AnnouncementsInfoByOffsetResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional(readOnly = true)
class GetAllAnnouncement(private val announcementRepository: AnnouncementRepository) : GetAllAnnouncementApi {
    //TODO: 패스워드 레포지터리 및 디코더 구현
    override fun findAllAnnouncementByOffset(
        authorization: String,
        page: Int,
        size: Int,
    ): ResponseEntity<AnnouncementsInfoByOffsetResponse> {
        val announcements: Page<Announcement> = announcementRepository.findAll(PageRequest.of(page, size))

        return ResponseEntity.ok(
            AnnouncementsInfoByOffsetResponse(
                announcements.get().map(::toAnnouncementPageResponses).toList(),
                announcements.number,
                announcements.size,
                announcements.totalElements.toInt(),
                announcements.totalPages
            )
        )
    }

    fun toAnnouncementPageResponses(announcement: Announcement): AnnouncementInfoResponse {
        return AnnouncementInfoResponse(
            announcement.id.toInt(),
            announcement.title,
            announcement.author,
            announcement.createdAt.toString()
        )
    }

    override fun findAllAnnouncementByCursor(
        authorization: String,
        cursorId: Int,
        size: Int
    ): ResponseEntity<AnnouncementsInfoByCursorResponse> {
        val pageRequest = PageRequest.of(0, size)
        val announcements: Slice<Announcement> = getAnnouncements(cursorId, pageRequest)

        return ResponseEntity.ok(
            AnnouncementsInfoByCursorResponse(
                announcements.get().map(::toAnnouncementPageResponses).toList(),
                announcements.hasNext(),
                announcements.last().id.toInt()
            )
        )
    }

    fun getAnnouncements(cursorId: Int, pageRequest: PageRequest): Slice<Announcement> {
        return when (cursorId) {
            0 -> announcementRepository.findByOrderByIdDesc(pageRequest)
            else -> announcementRepository.findByIdLessThanOrderByIdDesc(cursorId.toLong(), pageRequest)
        }
    }
}
