package com.woowahan.campus.announcement.domain

import jakarta.persistence.Embeddable

@Embeddable
class Title(val title: String) {
    init {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty() || trimmedTitle.length > MAX_LENGTH) {
            throw IllegalArgumentException("공지의 제목은 1자 이상, 50자 이하로 작성 가능합니다.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Title

        return title == other.title
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    companion object {
        private const val MAX_LENGTH: Int = 50
    }
}
