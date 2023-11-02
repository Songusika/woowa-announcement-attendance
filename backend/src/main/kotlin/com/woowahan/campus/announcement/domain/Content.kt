package com.woowahan.campus.announcement.domain

import jakarta.persistence.Embeddable

@Embeddable
class Content(val content: String) {
    init {
        val trimmedContent = content.trim()
        if (trimmedContent.isEmpty() || trimmedContent.length > MAX_LENGTH) {
            throw IllegalArgumentException("공지의 내용은 1자 이상, 65,535자 이하로 작성 가능합니다.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Content

        return content == other.content
    }

    override fun hashCode(): Int {
        return content.hashCode()
    }

    companion object {
        private const val MAX_LENGTH: Int = 65_535
    }
}
