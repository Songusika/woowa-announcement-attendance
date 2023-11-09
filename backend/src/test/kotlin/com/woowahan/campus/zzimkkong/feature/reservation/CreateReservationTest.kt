package com.woowahan.campus.zzimkkong.feature.reservation

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.ReservationFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkStatic
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.ReservationPost
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import java.time.LocalDateTime
import java.time.LocalTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateReservationTest(
    @LocalServerPort
    val port: Int,
    val spaceRepository: SpaceRepository,
    val campusRepository: CampusRepository,
) : BehaviorSpec({

    RestAssured.port = port

    beforeTest {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.parse("2023-11-07T00:00:00.000")
    }

    Given("캠퍼스와 회의실이 생성된다.") {
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = spaceRepository.save(SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2)))

        When("예약을 생성한다.") {
            val reservation = ReservationFixture.회의실_예약("2023-11-07", "11:00", "12:00")
            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(
                    ReservationPost(
                        "${reservation.date}T${reservation.startTime}",
                        "${reservation.date}T${reservation.endTime}",
                        reservation.name,
                        reservation.description,
                        reservation.password
                    )
                )
                .`when`().post("/api/maps/${campus.id}/spaces/${space.id}/reservations")
                .then().log().all()
                .extract()

            Then("201 응답과 생성된 예약의 URI를 Location Header로 반환한다.") {
                response.statusCode() shouldBe 201
                response.header(HttpHeaders.LOCATION) shouldNotBe null
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
