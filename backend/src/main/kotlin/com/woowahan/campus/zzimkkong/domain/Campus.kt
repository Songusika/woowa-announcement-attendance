package com.woowahan.campus.zzimkkong.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Campus(
    val name: String,
    val drawing: String,
    val thumbnail: String,
    @Id val id: Long = 0L,
) {
    fun withId(id: Long): Campus = Campus(name, drawing, thumbnail, id)
}
