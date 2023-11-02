package com.woowahan.campus.zzimkkong.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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
    init {
        validateTime(startTime, endTime)
        validateEnableDays(enableDays)
    }

    private fun validateTime(startTime: LocalTime, endTime: LocalTime) {
        require(!startTime.isAfter(endTime)) { "시작 시간이 종료 시간보다 늦을 수 없습니다." }
        require(startTime != endTime) { "시작 시간과 종료 시간이 같을 수 없습니다." }
    }

    private fun validateEnableDays(enableDays: String) {
        val days = DayOfWeeks.from(enableDays.split(","))
        require(days.size == days.toSet().size) { "중복된 요일이 있습니다." }
    }

    fun getEnableDays(): List<DayOfWeeks> {
        return DayOfWeeks.from(enableDays.split(","))
    }
}
