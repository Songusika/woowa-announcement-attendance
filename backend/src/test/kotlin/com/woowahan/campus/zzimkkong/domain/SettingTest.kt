package com.woowahan.campus.zzimkkong.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import java.time.LocalTime

class SettingTest : StringSpec({

    "시작 시간이 종료 시간보다 빠르면 예외가 발생하지 않는다." {
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)

        Setting(
            spaceId = 1L,
            startTime = startTime,
            endTime = endTime,
            maximumMinute = 30,
            enableDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY"
        )
    }

    "시작 시간이 종료 시간보다 늦으면 예외가 발생한다." {
        val startTime = LocalTime.of(10, 0)
        val endTime = LocalTime.of(9, 0)

        shouldThrow<IllegalArgumentException> {
            Setting(
                spaceId = 1L,
                startTime = startTime,
                endTime = endTime,
                maximumMinute = 30,
                enableDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY"
            )
        }
    }

    "시작 시간과 종료 시간이 같으면 예외가 발생한다." {
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(9, 0)

        shouldThrow<IllegalArgumentException> {
            Setting(
                spaceId = 1L,
                startTime = startTime,
                endTime = endTime,
                maximumMinute = 30,
                enableDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY"
            )
        }
    }

    "중복된 요일이 있으면 예외가 발생한다." {
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)

        shouldThrow<IllegalArgumentException> {
            Setting(
                spaceId = 1L,
                startTime = startTime,
                endTime = endTime,
                maximumMinute = 30,
                enableDays = "MONDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY"
            )
        }
    }

    "요일의 형식에 맞지 않는 문자열이 있으면 예외가 발생한다." {
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)

        shouldThrow<IllegalArgumentException> {
            Setting(
                spaceId = 1L,
                startTime = startTime,
                endTime = endTime,
                maximumMinute = 30,
                enableDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY,NOT_A_DAY"
            )
        }
    }
})
