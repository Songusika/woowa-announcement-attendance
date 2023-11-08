package com.woowahan.campus.zzimkkong.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate
import java.time.LocalTime

@Entity
class Reservation(
    val spaceId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val name: String,
    val description: String,
    val password: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) {
    fun isContain(otherReservation: Reservation): Boolean {
        return isContain(otherReservation.startTime, otherReservation.endTime)
    }

    fun isContain(startTime: LocalTime, endTime: LocalTime): Boolean {
        return (this.startTime <= startTime && startTime < this.endTime) ||
            (this.startTime < endTime && endTime <= this.endTime)
    }

    fun checkPassword(password: String) {
        require(this.password == password) { "예약 비밀번호가 일치하지 않습니다." }
    }
}
