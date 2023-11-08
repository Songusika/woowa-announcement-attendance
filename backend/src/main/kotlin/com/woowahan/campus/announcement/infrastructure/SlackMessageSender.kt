package com.woowahan.campus.announcement.infrastructure

import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.woowahan.campus.announcement.domain.Author
import com.woowahan.campus.announcement.domain.Content
import com.woowahan.campus.announcement.domain.MessageSender
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SlackMessageSender(
    @Value("\${slack.bot.token}") private val botToken: String,
) : MessageSender {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun sendMessage(to: String, author: Author, content: Content) {
        runCatching {
            val slack = Slack.getInstance().methods(botToken)

            val request = ChatPostMessageRequest.builder()
                .channel(to)
                .text(content.content)
                .build()

            slack.chatPostMessage(request)
        }.onFailure {
            log.error("Slack Send Error: {}", it.message, it)
        }
    }
}
