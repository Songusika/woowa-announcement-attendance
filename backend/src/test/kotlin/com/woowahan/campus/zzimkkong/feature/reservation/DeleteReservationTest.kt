package com.woowahan.campus.zzimkkong.feature.reservation

import com.woowahan.campus.zzimkkong.domain.Campus
import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.Reservation
import com.woowahan.campus.zzimkkong.domain.ReservationRepository
import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.Space
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.ReservationFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import openapi.model.RemoveReservationRequest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.LocalTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteReservationTest(
    @LocalServerPort
    val port: Int,
    val reservationRepository: ReservationRepository,
    val spaceRepository: SpaceRepository,
    val campusRepository: CampusRepository,
) : BehaviorSpec({

    RestAssured.port = port

    Given("예약을 생성한다.") {
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("SATURDAY,SUNDAY", "15:00", "20:00", 30)
        val space = spaceRepository.save(SpaceFixture.굿샷_강의장(0L, true, listOf(setting1, setting2)))
        val reservation =
            reservationRepository.save(ReservationFixture.회의실_예약(space.id, "2023-11-07", "11:00", "12:00", "1234"))

        When("예약 삭제시 비밀번호가 다르다.") {
            val response = deleteReservation(campus, space, reservation, RemoveReservationRequest("invalidPassword"))

            Then("200 이외의 응답을 반환한다.") {
                response.statusCode() shouldNotBe 200
            }
        }

        When("예약을 삭제한다.") {
            val response = deleteReservation(campus, space, reservation, RemoveReservationRequest("1234"))

            Then("204 응답을 반환한다.") {
                response.statusCode() shouldBe 204
            }

            Then("예약 조회 시 존재하지 않는다.") {
                reservationRepository.findById(reservation.id) shouldBe null
            }
        }
    }
})

private fun deleteReservation(
    campus: Campus,
    space: Space,
    reservation: Reservation,
    request: RemoveReservationRequest,
): ExtractableResponse<Response> = RestAssured
    .given().log().all()
    .contentType(ContentType.JSON)
    .body(request)
    .`when`().delete("/api/maps/${campus.id}/spaces/${space.id}/reservations/${reservation.id}")
    .then().log().all()
    .extract()

private fun getSetting(enableDays: String, startTime: String, endTime: String, maximumMinute: Int) = Setting(
    startTime = LocalTime.parse(startTime),
    endTime = LocalTime.parse(endTime),
    maximumMinute = maximumMinute,
    enableDays = enableDays
)
