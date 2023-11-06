package com.woowahan.campus.zzimkkong.domain

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
                    (it.startTime <= startTime) &&
                        (it.endTime >= endTime) &&
                        (startTime.plusMinutes(it.maximumMinute.toLong()) >= endTime)
                }
            ) { "예약이 불가능한 시간입니다." }

            findReservations.forEach {
                require(it.isContain(reservation).not()) { "시간이 겹칩니다." }
            }
        }
    }
}
