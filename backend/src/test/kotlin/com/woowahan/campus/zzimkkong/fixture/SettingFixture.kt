package com.woowahan.campus.zzimkkong.fixture

import com.woowahan.campus.zzimkkong.domain.Setting
import java.time.LocalTime

class SettingFixture {
    companion object {

        fun 회의실_예약_설정_1() = Setting(
            spaceId = 0L,
            startTime = LocalTime.of(10, 0, 0),
            endTime = LocalTime.of(11, 0, 0),
            maximumMinute = 60,
            enableDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY",
        )

        fun 회의실_예약_설정_2() = Setting(
            spaceId = 0L,
            startTime = LocalTime.of(11, 0, 0),
            endTime = LocalTime.of(12, 0, 0),
            maximumMinute = 60,
            enableDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY",
        )

        fun 회의실_예약_설정_3() = Setting(
            spaceId = 0L,
            startTime = LocalTime.of(9, 0, 0),
            endTime = LocalTime.of(16, 0, 0),
            maximumMinute = 60,
            enableDays = "SATURDAY,SUNDAY",
        )

        fun 회의실_예약_설정_4() = Setting(
            spaceId = 0L,
            startTime = LocalTime.of(10, 0, 0),
            endTime = LocalTime.of(18, 0, 0),
            maximumMinute = 60,
            enableDays = "MONDAY,WEDNESDAY,FRIDAY,SUNDAY",
        )
    }
}
