package com.woowahan.campus.zzimkkong.domain

import java.time.LocalDateTime

class ReservationValidator {

    companion object {
        fun validate(space: Space, reservation: Reservation, findReservations: List<Reservation>) {
            val startTime = reservation.startTime
            val endTime = reservation.endTime

            require(space.reservationEnabled) { "예약이 불가능한 공간입니다." }

            val validSettings = space.settings.filter {
                it.getEnableDays().contains(DayOfWeeks.valueOf(reservation.date.dayOfWeek.name))
            }

            require(
                validSettings.any {
                    it.isEnableTime(startTime, endTime)
                }
            ) { "예약이 불가능한 시간입니다." }

            findReservations.forEach {
                require(it.isContain(reservation).not()) { "시간이 겹칩니다." }
            }
        }

        fun validateTime(startDateTime: LocalDateTime, endDateTime: LocalDateTime, now: LocalDateTime) {
            require(startDateTime.toLocalDate() == endDateTime.toLocalDate()) { "시작 날짜와 종료 날짜는 동일해야 합니다." }
            require(startDateTime >= now) { "현재 시간 이후로 예약해야 합니다." }
        }
    }
}
