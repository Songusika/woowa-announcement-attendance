package com.woowahan.campus.zzimkkong.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Campus(
    val name: String,
    val drawing: String,
    var thumbnail: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) {
    fun updateThumbnail(thumbnail: String) {
        this.thumbnail = thumbnail
    }
}
