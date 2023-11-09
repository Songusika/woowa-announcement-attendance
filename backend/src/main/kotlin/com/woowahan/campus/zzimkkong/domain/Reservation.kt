package com.woowahan.campus.zzimkkong.domain

import com.woowahan.campus.zzimkkong.support.BaseRootEntity
import jakarta.persistence.Entity
import jakarta.persistence.PostPersist
import jakarta.persistence.PreRemove
import java.time.LocalDate
import java.time.LocalTime

data class CreatedReservationEvent(val reservation: Reservation)
data class UpdatedReservationEvent(val reservation: Reservation)
data class DeletedReservationEvent(val reservation: Reservation)

@Entity
class Reservation(
    val spaceId: Long,
    val date: LocalDate,
    var startTime: LocalTime,
    var endTime: LocalTime,
    var name: String,
    var description: String,
    var password: String,
    id: Long = 0L,
) : BaseRootEntity<Reservation>(id) {

    @PostPersist
    fun persist() {
        registerEvent(CreatedReservationEvent(this))
    }

    fun update(
        startTime: LocalTime,
        endTime: LocalTime,
        name: String,
        description: String,
        password: String,
    ): Reservation {
        this.startTime = startTime
        this.endTime = endTime
        this.name = name
        this.description = description
        this.password = password
        registerEvent(UpdatedReservationEvent(this))
        return this
    }

    @PreRemove
    fun remove() {
        registerEvent(DeletedReservationEvent(this))
    }

    fun isContain(otherReservation: Reservation): Boolean {
        return isContain(otherReservation.startTime, otherReservation.endTime)
    }

    fun isContain(startTime: LocalTime, endTime: LocalTime): Boolean {
        return startTime in startTimeRange || endTime in endTimeRange
    }

    private val startTimeRange: ClosedRange<LocalTime>
        get() = this.startTime..this.endTime.minusMinutes(1)

    private val endTimeRange: ClosedRange<LocalTime>
        get() = this.startTime.plusMinutes(1)..this.endTime

    fun checkPassword(password: String) {
        require(this.password == password) { "예약 비밀번호가 일치하지 않습니다." }
    }
}
