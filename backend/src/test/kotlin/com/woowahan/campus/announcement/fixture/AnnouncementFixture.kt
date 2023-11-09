package com.woowahan.campus.announcement.fixture

import com.woowahan.campus.announcement.domain.Announcement
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannel
import com.woowahan.campus.announcement.domain.Author
import com.woowahan.campus.announcement.domain.Content
import com.woowahan.campus.announcement.domain.Title
import openapi.model.AnnouncementInfoResponse
import openapi.model.AnnouncementResponse
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
    announcements: List<AnnouncementInfoResponse>,
    page: Int,
    size: Int,
    totalElements: Int,
    totalPages: Int,
): AnnouncementsInfoByOffsetResponse {
    return AnnouncementsInfoByOffsetResponse(
        announcements,
        page,
        size,
        totalElements,
        totalPages,
    )
}

fun createAnnouncementsInfoByCursorResponse(
    announcements: List<AnnouncementInfoResponse>,
    hasNext: Boolean,
    lastCursorId: Long,
): AnnouncementsInfoByCursorResponse {
    return AnnouncementsInfoByCursorResponse(
        announcements,
        hasNext,
        lastCursorId,
    )
}

fun createAnnouncementInfoResponses(
    announcements: List<Announcement>,
    slackChannel: AnnouncementSlackChannel,
): List<AnnouncementInfoResponse> {
    return announcements.map { createAnnouncementInfoResponse(it, slackChannel) }
}

fun createAnnouncementInfoResponse(
    announcement: Announcement,
    slackChannel: AnnouncementSlackChannel,
): AnnouncementInfoResponse {
    return AnnouncementInfoResponse(
        announcement.id,
        announcement.title.title,
        announcement.author.author,
        announcement.createdAt.toString(),
        slackChannel.name,
    )
}

fun createAnnouncementRequest(
    title: String = "title",
    content: String = "content",
    author: String,
    slackChannelId: Long = 1L,
    slackChannelName: String = "slackChannelName",
): CreateAnnouncementRequest {
    return CreateAnnouncementRequest(
        title,
        content,
        author,
        CreateAnnouncementRequestSlackChannel(slackChannelId, slackChannelName),
    )
}

fun createAnnouncementResponse(
    announcement: Announcement,
    slackChannel: AnnouncementSlackChannel,
): AnnouncementResponse {
    return AnnouncementResponse(
        announcement.id,
        announcement.title.title,
        announcement.content.content,
        announcement.author.author,
        announcement.createdAt.toString(),
        slackChannel.name,
    )
}

fun createUpdateAnnouncementRequest(
    title: String = "updateTitle",
    content: String = "updateContent",
    author: String,
): UpdateAnnouncementRequest {
    return UpdateAnnouncementRequest(title, content, author)
}
