package com.woowahan.campus.announcement.domain

import jakarta.persistence.Embeddable

@Embeddable
data class Author(
    val author: String,
) {
    init {
        require(author.isNotEmpty() && author.length <= MAX_LENGTH) {
            "공지의 작성자는 1자 이상, ${MAX_LENGTH}자 이하로 작성 가능합니다."
        }
    }

    companion object {
        private const val MAX_LENGTH: Int = 20
    }
}
