package com.woowahan.campus.announcement.domain

import jakarta.persistence.Embeddable

@Embeddable
class Author(val author: String) {
    init {
        val trimmedAuthor = author.trim()
        if (trimmedAuthor.isEmpty() || trimmedAuthor.length > MAX_LENGTH) {
            throw IllegalArgumentException("공지의 작성자는 1자 이상, 20자 이하로 작성 가능합니다.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Author

        return author == other.author
    }

    override fun hashCode(): Int {
        return author.hashCode()
    }

    companion object {
        private const val MAX_LENGTH: Int = 20
    }
}
