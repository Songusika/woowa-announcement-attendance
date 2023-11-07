package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.AnnouncementRepository
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannelRepository
import com.woowahan.campus.announcement.domain.getById
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
class GetAllAnnouncement(
    private val announcementRepository: AnnouncementRepository,
    private val announcementSlackChannelRepository: AnnouncementSlackChannelRepository,
) : GetAllAnnouncementApi {

    override fun findAllAnnouncementByOffset(
        authorization: String,
        page: Int,
        size: Int,
    ): ResponseEntity<AnnouncementsInfoByOffsetResponse> {
        val announcements: Page<Announcement> = announcementRepository.findAllBy(PageRequest.of(page, size))

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
            announcement.id,
            announcement.title.title,
            announcement.author.author,
            announcement.createdAt.toString(),
            announcementSlackChannelRepository.getById(announcement.slackChannelId).name
        )
    }

    override fun findAllAnnouncementByCursor(
        authorization: String,
        cursorId: Int,
        size: Int
    ): ResponseEntity<AnnouncementsInfoByCursorResponse> {
        val pageRequest = PageRequest.of(0, size)
        val announcements: Slice<Announcement> = getAnnouncements(cursorId.toLong(), pageRequest)

        return ResponseEntity.ok(
            AnnouncementsInfoByCursorResponse(
                announcements.get().map(::toAnnouncementPageResponses).toList(),
                announcements.hasNext(),
                announcements.last().id
            )
        )
    }

    fun getAnnouncements(cursorId: Long, pageRequest: PageRequest): Slice<Announcement> {
        return when (cursorId) {
            0L -> announcementRepository.findByOrderByIdDesc(pageRequest)
            else -> announcementRepository.findByIdLessThanOrderByIdDesc(cursorId, pageRequest)
        }
    }
}
