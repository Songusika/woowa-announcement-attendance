package com.woowahan.campus.announcement.domain

interface MessageSender {

    fun sendMessage(to: String, author: Author, content: Content)
}
