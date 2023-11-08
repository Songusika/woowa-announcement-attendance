package com.woowahan.campus.announcement.domain

import jakarta.persistence.Embeddable

@Embeddable
data class Content(
    val content: String,
) {
    init {
        require(content.isNotEmpty() && content.length <= MAX_LENGTH) {
            "공지의 내용은 1자 이상, ${MAX_LENGTH}자 이하로 작성 가능합니다."
        }
    }

    companion object {
        private const val MAX_LENGTH: Int = 65_535
    }
}
