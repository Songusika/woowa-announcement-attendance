package com.woowahan.campus.zzimkkong.domain

import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import java.time.LocalTime

class SpaceValidatorTest : StringSpec({

    "Setting 설정들의 요일 + 시간이 겹치면 예외가 발생한다." {
        val setting1 = getSetting("MONDAY", "11:00", "13:00")
        val setting2 = getSetting("MONDAY", "12:00", "14:00")
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))

        shouldThrow<IllegalArgumentException> {
            SpaceSettingsValidator.validate(space)
        }
    }

    "Setting 설정들의 요일만 겹치면 예외가 발생하지 않는다." {
        val setting1 = getSetting("MONDAY", "11:00", "13:00")
        val setting2 = getSetting("MONDAY", "14:00", "16:00")
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))

        shouldNotThrow<Exception> {
            SpaceSettingsValidator.validate(space)
        }
    }

    "Setting 설정들의 시간만 겹치면 예외가 발생하지 않는다." {
        val setting1 = getSetting("MONDAY", "11:00", "13:00")
        val setting2 = getSetting("TUESDAY", "12:00", "14:00")
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))

        shouldNotThrow<Exception> {
            SpaceSettingsValidator.validate(space)
        }
    }

    "Setting 설정들의 종료시간과 시작시간이 같아도 예외가 발생하지 않는다." {
        val setting1 = getSetting("MONDAY", "11:00", "13:00")
        val setting2 = getSetting("MONDAY", "13:00", "14:00")
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))

        shouldNotThrow<Exception> {
            SpaceSettingsValidator.validate(space)
        }
    }

    "Setting 설정들의 요일이 같고 시간이 포함되는 경우 예외가 발생한다." {
        val setting1 = getSetting("MONDAY", "11:00", "18:00")
        val setting2 = getSetting("MONDAY", "12:00", "14:00")
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))

        shouldThrow<IllegalArgumentException> {
            SpaceSettingsValidator.validate(space)
        }
    }

    "Settings가 존재하지 않을 때 Space의 예약 가능 여부가 true면 예외가 발생한다." {
        val space = SpaceFixture.굿샷_강의장(0L, true, emptyList())

        shouldThrow<IllegalArgumentException> {
            SpaceSettingsValidator.validate(space)
        }
    }
})

private fun getSetting(enableDays: String, startTime: String, endTime: String) = Setting(
    startTime = LocalTime.parse(startTime),
    endTime = LocalTime.parse(endTime),
    maximumMinute = 30,
    enableDays = enableDays
)
