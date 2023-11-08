package com.woowahan.campus.zzimkkong.feature.reservation

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.ReservationRepository
import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.getById
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.ReservationFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkStatic
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import openapi.model.ReservationPut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.LocalDateTime
import java.time.LocalTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateReservationTest(
    @LocalServerPort
    val port: Int,
    @Autowired
    val reservationRepository: ReservationRepository,
    @Autowired
    val spaceRepository: SpaceRepository,
    @Autowired
    val campusRepository: CampusRepository,
) : BehaviorSpec({

    RestAssured.port = port

    beforeTest {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.parse("2023-11-07T00:00:00.000")
    }

    Given("예약을 생성한다.") {
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = spaceRepository.save(SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2)))
        val reservation = reservationRepository.save(ReservationFixture.회의실_예약(space.id, "2023-11-07", "11:00", "12:00"))

        When("예약을 수정한다.") {
            val updateRequest = ReservationPut(
                "2023-11-07T12:00",
                "2023-11-07T13:00",
                "브리",
                "수료 기념 회식",
                "1234"
            )
            val response = updateReservation(updateRequest, campus.id, space.id, reservation.id)

            Then("200 응답을 반환한다.") {
                response.statusCode() shouldBe 200
            }

            Then("예약의 수정사항이 반영된다.") {
                val updateResponse = reservationRepository.getById(reservation.id)
                assertSoftly {
                    updateResponse.name shouldBe updateRequest.name
                    updateResponse.description shouldBe updateRequest.description
                    "${updateResponse.date}T${updateResponse.startTime}" shouldBe updateRequest.startDateTime
                    "${updateResponse.date}T${updateResponse.endTime}" shouldBe updateRequest.endDateTime
                }
            }
        }

        When("예약 수정시 비밀번호가 다르다.") {
            val updateRequest = ReservationPut(
                "2023-11-07T12:00",
                "2023-11-07T13:00",
                "브리",
                "수료 기념 회식",
                "invalidPassword"
            )
            val response = updateReservation(updateRequest, campus.id, space.id, reservation.id)

            Then("200 이외의 응답을 반환한다.") {
                response.statusCode() shouldNotBe 200
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

private fun updateReservation(
    updateRequest: ReservationPut,
    campusId: Long,
    spaceId: Long,
    reservationId: Long,
): ExtractableResponse<Response> = RestAssured
    .given().log().all()
    .contentType(ContentType.JSON)
    .body(updateRequest)
    .`when`().put("/api/maps/$campusId/spaces/$spaceId/reservations/$reservationId")
    .then().log().all()
    .extract()
