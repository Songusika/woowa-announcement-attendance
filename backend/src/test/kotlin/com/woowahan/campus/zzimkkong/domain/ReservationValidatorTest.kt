package com.woowahan.campus.zzimkkong.domain

import com.woowahan.campus.zzimkkong.fixture.ReservationFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import java.time.LocalTime

class ReservationValidatorTest : StringSpec({

    "회의실이 예약 가능한 상태가 아니면 예외가 발생한다." {
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = SpaceFixture.굿샷_강의장(0L, false, listOf(setting1, setting2))
        val reservations = listOf(
            ReservationFixture.회의실_예약("2023-11-06", "11:30", "12:00"),
            ReservationFixture.회의실_예약("2023-11-06", "12:00", "13:00")
        )

        val reservation = ReservationFixture.회의실_예약("2023-11-06", "12:00", "13:00")

        shouldThrow<IllegalArgumentException> {
            ReservationValidator.validate(
                space, reservation, reservations
            )
        }
    }

    "회의실이 예약 가능한 상태면 아니면 예외가 발생하지 않는다." {
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))
        val reservations = listOf(
            ReservationFixture.회의실_예약("2023-11-06", "11:30", "12:00"),
            ReservationFixture.회의실_예약("2023-11-06", "12:00", "13:00")
        )

        assertSoftly {
            shouldNotThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "13:00", "14:00"), reservations
                )
            }

            shouldNotThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "11:00", "11:30"), reservations
                )
            }
        }
    }

    "예약이 불가능한 요일이면 예외가 발생한다." {
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))
        val reservations = listOf(
            ReservationFixture.회의실_예약("2023-11-06", "11:30", "12:00"),
            ReservationFixture.회의실_예약("2023-11-06", "12:00", "13:00")
        )

        assertSoftly {
            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-08", "13:00", "14:00"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-09", "13:00", "14:00"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-10", "13:00", "14:00"), reservations
                )
            }
        }
    }

    "회의실 예약가능 시간을 넘어가면 예외가 발생한다." {
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))
        val reservations = listOf(
            ReservationFixture.회의실_예약("2023-11-06", "11:30", "12:00"),
            ReservationFixture.회의실_예약("2023-11-06", "12:00", "13:00")
        )

        assertSoftly {
            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "10:30", "11:30"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "13:30", "14:30"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "10:00", "11:00"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "14:00", "15:00"), reservations
                )
            }
        }
    }

    "설정의 최대 예약가능 시간을 넘어가면 예외가 발생한다." {
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))
        val reservations = listOf(
            ReservationFixture.회의실_예약("2023-11-06", "11:30", "12:00"),
            ReservationFixture.회의실_예약("2023-11-06", "12:00", "13:00")
        )

        assertSoftly {
            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-07", "11:00", "12:01"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-07", "12:59", "14:00"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-11", "15:00", "15:31"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-11", "19:29", "20:00"), reservations
                )
            }
        }
    }

    "기존 예약과 시간이 겹치면 예외가 발생한다." {
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))
        val reservations = listOf(
            ReservationFixture.회의실_예약("2023-11-06", "11:30", "12:00"),
            ReservationFixture.회의실_예약("2023-11-06", "12:00", "13:00")
        )

        assertSoftly {
            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "12:00", "12:30"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "12:30", "13:00"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "11:30", "12:01"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "11:00", "11:31"), reservations
                )
            }

            shouldThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "12:59", "13:30"), reservations
                )
            }
        }
    }

    "예약이 성공한다." {
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2))
        val reservations = listOf(
            ReservationFixture.회의실_예약("2023-11-06", "11:30", "12:00"),
            ReservationFixture.회의실_예약("2023-11-06", "12:00", "13:00")
        )

        assertSoftly {
            shouldNotThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "11:00", "11:30"), reservations
                )
            }

            shouldNotThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-06", "13:00", "14:00"), reservations
                )
            }

            shouldNotThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-11", "15:00", "15:30"), reservations
                )
            }

            shouldNotThrow<IllegalArgumentException> {
                ReservationValidator.validate(
                    space, ReservationFixture.회의실_예약("2023-11-12", "19:30", "20:00"), reservations
                )
            }
        }
    }
})

private fun getSetting(enableDays: String, startTime: String, endTime: String, maximumMinute: Int) = Setting(
    startTime = LocalTime.parse(startTime),
    endTime = LocalTime.parse(endTime),
    maximumMinute = maximumMinute,
    enableDays = enableDays
)
