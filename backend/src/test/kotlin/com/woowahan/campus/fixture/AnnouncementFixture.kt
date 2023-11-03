package com.woowahan.campus.fixture

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.Author
import com.woowahan.campus.announcement.domain.Content
import com.woowahan.campus.announcement.domain.Title
import openapi.model.AnnouncementInfoResponse
import openapi.model.AnnouncementsInfoByCursorResponse
import openapi.model.AnnouncementsInfoByOffsetResponse

fun createAnnouncement(
    title: String,
    content: String,
    author: String,
    slackChannelId: Int,
    id: Long = 0L,
): Announcement {
    return Announcement(Title(title), Content(content), Author(author), slackChannelId, id)
}

fun createAnnouncementsInfoByOffsetResponse(
    announcements: List<Announcement>,
    page: Int,
    size: Int,
    totalElements: Int,
    totalPages: Int,
): AnnouncementsInfoByOffsetResponse {
    return AnnouncementsInfoByOffsetResponse(
        announcements.map { createAnnouncementInfoResponse(it) },
        page,
        size,
        totalElements,
        totalPages
    )
}

fun createAnnouncementsInfoByCursorResponse(
    announcements: List<Announcement>,
    hasNext: Boolean,
    lastCursorId: Int,
): AnnouncementsInfoByCursorResponse {
    return AnnouncementsInfoByCursorResponse(
        announcements.map { createAnnouncementInfoResponse(it) },
        hasNext,
        lastCursorId
    )
}

fun createAnnouncementInfoResponse(announcement: Announcement): AnnouncementInfoResponse {
    return AnnouncementInfoResponse(
        announcement.id.toInt(),
        announcement.title.title,
        announcement.author.author,
        announcement.createdAt.toString()
    )
}
