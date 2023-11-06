package com.woowahan.campus.zzimkkong.fixture

import com.woowahan.campus.zzimkkong.domain.Reservation
import java.time.LocalDate
import java.time.LocalTime

class ReservationFixture {

    companion object {
        fun 회의실_예약(
            date: String,
            startTime: String,
            endTime: String,
        ): Reservation {
            return Reservation(
                spaceId = 0L,
                date = LocalDate.parse(date),
                startTime = LocalTime.parse(startTime),
                endTime = LocalTime.parse(endTime),
                name = "회의실 예약",
                description = "회의실 예약 설명",
                password = "1234"
            )
        }
    }
}
