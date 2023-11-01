package com.woowahan.campus.zzimkkong.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Space(
    val campusId: Long,
    var name: String,
    var color: String,
    var area: String,
    var reservationEnabled: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) {
    fun update(
        name: String,
        color: String,
        area: String,
        reservationEnabled: Boolean,
    ) {
        this.name = name
        this.color = color
        this.area = area
        this.reservationEnabled = reservationEnabled
    }
}
