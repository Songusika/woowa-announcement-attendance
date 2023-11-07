package com.woowahan.campus.fixture

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.Author
import com.woowahan.campus.announcement.domain.Content
import com.woowahan.campus.announcement.domain.Title
import openapi.model.AnnouncementInfoResponse
import openapi.model.AnnouncementsInfoByCursorResponse
import openapi.model.AnnouncementsInfoByOffsetResponse
import openapi.model.CreateAnnouncementRequest
import openapi.model.CreateAnnouncementRequestSlackChannel
import openapi.model.UpdateAnnouncementRequest

fun createAnnouncement(
    title: String,
    content: String,
    author: String,
    slackChannelId: Long,
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
    lastCursorId: Long,
): AnnouncementsInfoByCursorResponse {
    return AnnouncementsInfoByCursorResponse(
        announcements.map { createAnnouncementInfoResponse(it) },
        hasNext,
        lastCursorId
    )
}

fun createAnnouncementInfoResponse(announcement: Announcement): AnnouncementInfoResponse {
    return AnnouncementInfoResponse(
        announcement.id,
        announcement.title.title,
        announcement.author.author,
        announcement.createdAt.toString()
    )
}

fun createAnnouncementRequest(
    title: String = "title",
    content: String = "content",
    author: String,
    slackChannelId: Long = 1L,
    slackChannelName: String = "slackChannelName"
): CreateAnnouncementRequest {
    return CreateAnnouncementRequest(
        title,
        content,
        author,
        CreateAnnouncementRequestSlackChannel(slackChannelId, slackChannelName)
    )
}

fun createUpdateAnnouncementRequest(
    title: String = "updateTitle",
    content: String = "updateContent",
    author: String
): UpdateAnnouncementRequest {
    return UpdateAnnouncementRequest(title, content, author)
}
