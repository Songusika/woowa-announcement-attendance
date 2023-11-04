package com.woowahan.campus.announcement.domain

import jakarta.persistence.Embeddable

@Embeddable
data class Author(val author: String) {
    init {
        val trimmedAuthor = author.trim()
        if (trimmedAuthor.isEmpty() || trimmedAuthor.length > MAX_LENGTH) {
            throw IllegalArgumentException("공지의 작성자는 1자 이상, 20자 이하로 작성 가능합니다.")
        }
    }

    companion object {
        private const val MAX_LENGTH: Int = 20
    }
}
