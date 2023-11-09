package com.woowahan.campus.zzimkkong.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Entity
class Space(
    val campusId: Long,
    var name: String,
    var color: String,
    var area: String,
    var reservationEnabled: Boolean,
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE], orphanRemoval = true)
    @JoinColumn(name = "space_id", nullable = false, updatable = false)
    val settings: MutableList<Setting>,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) {
    fun update(
        name: String,
        color: String,
        area: String,
        reservationEnabled: Boolean,
        settings: List<Setting>,
    ) {
        this.name = name
        this.color = color
        this.area = area
        this.reservationEnabled = reservationEnabled
        this.settings.clear()
        this.settings.addAll(settings)
    }
}
