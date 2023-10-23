package com.woowahan.campus.zzimkkong.domain

import org.springframework.data.annotation.Id

class Campus(
    val name: String,
    val drawing: String,
    val thumbnail: String,
    @Id var id: Long = 0L,
)
