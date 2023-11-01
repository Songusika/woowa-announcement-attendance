package com.woowahan.campus.zzimkkong.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.DayOfWeek
import java.time.LocalTime

@Entity
class Setting(
    val spaceId: Long,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val maximumMinute: Int,
    private val enableDays: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) {
    fun getEnableDays(): List<DayOfWeek> {
        return enableDays.split(",")
            .map { DayOfWeek.valueOf(it) }
            .toList()
    }
}
