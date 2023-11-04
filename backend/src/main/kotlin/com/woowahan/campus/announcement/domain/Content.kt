package com.woowahan.campus.announcement.domain

import jakarta.persistence.Embeddable

@Embeddable
data class Content(val content: String) {
    init {
        val trimmedContent = content.trim()
        require(trimmedContent.isNotEmpty() && trimmedContent.length <= MAX_LENGTH) {
            "공지의 내용은 1자 이상, 65,535자 이하로 작성 가능합니다."
        }
    }

    companion object {
        private const val MAX_LENGTH: Int = 65_535
    }
}
