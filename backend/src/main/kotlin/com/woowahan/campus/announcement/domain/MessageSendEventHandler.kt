package com.woowahan.campus.announcement.domain

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class MessageSendEventHandler(
    val slackChannelRepository: SlackChannelRepository,
    val messageSender: MessageSender,
) {

    @Async
    @EventListener
    fun handle(messageSendEvent: MessageSendEvent) {
        val slackChannel = slackChannelRepository.findById(messageSendEvent.channelId) ?: throw IllegalArgumentException()
        messageSender.sendMessage(
            slackChannel.providerId,
            messageSendEvent.author,
            messageSendEvent.content,
        )
    }
}
