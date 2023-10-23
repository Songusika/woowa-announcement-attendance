package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.AnnouncementSlackChannelRepository
import com.woowahan.campus.announcement.domain.MessageSendEvent
import com.woowahan.campus.announcement.domain.MessageSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class MessageSendEventHandler(
    val announcementSlackChannelRepository: AnnouncementSlackChannelRepository,
    val messageSender: MessageSender,
) {

    @Async
    @TransactionalEventListener
    fun handle(messageSendEvent: MessageSendEvent) {
        val slackChannel =
            announcementSlackChannelRepository.findById(messageSendEvent.channelId) ?: throw IllegalArgumentException()
        messageSender.sendMessage(
            slackChannel.providerId,
            messageSendEvent.author,
            messageSendEvent.content,
        )
    }
}
