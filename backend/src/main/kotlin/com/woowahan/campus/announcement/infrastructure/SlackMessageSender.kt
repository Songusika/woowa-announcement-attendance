package com.woowahan.campus.announcement.infrastructure

import com.slack.api.Slack
import com.woowahan.campus.announcement.domain.MessageSender
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SlackMessageSender(
    @Value("\${slack.user.token}") private val userToken: String,
) : MessageSender {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun sendMessage(to: String, author: String, content: String) {
        val slack = Slack.getInstance().methods()
        runCatching {
            slack.chatPostMessage {
                it.token(userToken)
                    .channel(to)
                    .text(content)
            }
        }.onFailure {
            log.error("Slack Send Error: {}", it.message, it)
        }
    }
}
