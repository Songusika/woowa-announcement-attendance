package com.woowahan.campus.zzimkkong.domain

import com.woowahan.campus.zzimkkong.fixture.ReservationFixture
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalTime

class ReservationTest : StringSpec({

    "예약 시간이 겹친다면 true를 반환한다." {
        val reservation = ReservationFixture.회의실_예약(0L, "2023-11-06", "12:00", "13:00", "password")

        assertSoftly {
            reservation.isContain(LocalTime.parse("11:00"), LocalTime.parse("12:01")) shouldBe true
            reservation.isContain(LocalTime.parse("12:59"), LocalTime.parse("14:00")) shouldBe true
        }
    }

    "예약 시간이 겹치지 않는다면 false를 반환한다." {
        val reservation = ReservationFixture.회의실_예약(0L, "2023-11-06", "12:00", "13:00", "password")

        assertSoftly {
            reservation.isContain(LocalTime.parse("11:00"), LocalTime.parse("12:00")) shouldBe false
            reservation.isContain(LocalTime.parse("13:00"), LocalTime.parse("14:00")) shouldBe false
        }
    }

    "비밀번호가 일치하지 않으면 예외가 발생한다." {
        val reservation = ReservationFixture.회의실_예약(0L, "2023-11-06", "12:00", "13:00", "password")

        shouldThrow<IllegalArgumentException> {
            reservation.checkPassword("invalidPassword")
        }
    }

    "비밀번호가 일치하면 예외가 발생하지 않는다." {
        val reservation = ReservationFixture.회의실_예약(0L, "2023-11-06", "12:00", "13:00", "password")

        shouldNotThrow<IllegalArgumentException> {
            reservation.checkPassword("password")
        }
    }
})
