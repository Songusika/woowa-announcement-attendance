package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.MessageSendEvent
import com.woowahan.campus.announcement.domain.MessageSender
import com.woowahan.campus.announcement.domain.SlackChannelRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class MessageSendEventHandler(
    val slackChannelRepository: SlackChannelRepository,
    val messageSender: MessageSender,
) {

    @Async
    @EventListener
    fun handle(messageSendEvent: MessageSendEvent) {
        val slackChannel =
            slackChannelRepository.findById(messageSendEvent.channelId) ?: throw IllegalArgumentException()
        messageSender.sendMessage(
            slackChannel.providerId,
            messageSendEvent.author,
            messageSendEvent.content,
        )
    }
}
