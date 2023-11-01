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
    val name: String,
    val color: String,
    val area: String,
    val reservationEnabled: Boolean,
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "space_id", nullable = false, updatable = false)
    val settings: List<Setting>,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
)
