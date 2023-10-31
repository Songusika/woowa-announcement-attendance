package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.MessageSendEvent
import com.woowahan.campus.announcement.domain.MessageSender
import com.woowahan.campus.announcement.domain.SlackChannelRepository
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
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(messageSendEvent: MessageSendEvent) {

        val slackChannel = slackChannelRepository.findById(messageSendEvent.channelId)
            ?: throw IllegalArgumentException()

        messageSender.sendMessage(
            slackChannel.providerId,
            messageSendEvent.author,
            messageSendEvent.content,
        )
    }
}
