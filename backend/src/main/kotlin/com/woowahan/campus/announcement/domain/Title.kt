package com.woowahan.campus.announcement.domain

import jakarta.persistence.Embeddable

@Embeddable
data class Title(val title: String) {
    init {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty() || trimmedTitle.length > MAX_LENGTH) {
            throw IllegalArgumentException("공지의 제목은 1자 이상, 50자 이하로 작성 가능합니다.")
        }
    }

    companion object {
        private const val MAX_LENGTH: Int = 50
    }
}
