package com.woowahan.campus.zzimkkong.feature.reservation

import com.woowahan.campus.support.DatabaseInitializer
import com.woowahan.campus.support.asPrettyJson
import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.ReservationRepository
import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.ReservationFixture.Companion.회의실_예약
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture.Companion.굿샷_강의장
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture.Companion.랜딩_강의장
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.LocalDateTime
import java.time.LocalTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReadReservationTest(
    @LocalServerPort
    val port: Int,
    val reservationRepository: ReservationRepository,
    val spaceRepository: SpaceRepository,
    val campusRepository: CampusRepository,
    val databaseInitializer: DatabaseInitializer,
) : BehaviorSpec({

    RestAssured.port = port

    extensions(databaseInitializer)

    beforeTest {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.parse("2023-11-07T00:00:00.000")
    }

    Given("캠퍼스와 회의실이 생성된다.") {
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        val setting1 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val setting2 = getSetting("MONDAY,TUESDAY", "11:00", "14:00", 60)
        val space1 = spaceRepository.save(굿샷_강의장(campus.id, true, listOf(setting1)))
        val space2 = spaceRepository.save(랜딩_강의장(campus.id, true, listOf(setting2)))
        val space3 = spaceRepository.save(랜딩_강의장(campus.id, false, emptyList()))
        val reservation1 = reservationRepository.save(회의실_예약(space1.id, "2023-11-07", "11:00", "12:00"))
        reservationRepository.save(회의실_예약(space1.id, "2023-11-07", "12:00", "13:00"))
        reservationRepository.save(회의실_예약(space2.id, "2023-11-07", "12:00", "13:00"))

        When("예약 가능 여부를 조회한다.") {
            val response = RestAssured
                .given().log().all()
                .params(
                    "startDateTime", "2023-11-07T11:00",
                    "endDateTime", "2023-11-07T12:00"
                )
                .`when`().get("/api/maps/{mapId}/spaces/availability", campus.id)
                .then().log().all()
                .extract()

            Then("200 응답과 공간의 예약 가능 여부를 반환한다.") {
                response.statusCode() shouldBe 200
                response.asPrettyJson() shouldBe
                    """
                        {
                            "mapId": 1,
                            "spaces": [
                                {
                                    "spaceId": ${space1.id},
                                    "isAvailable": false
                                },
                                {
                                    "spaceId": ${space2.id},
                                    "isAvailable": true
                                },
                                {
                                    "spaceId": ${space3.id},
                                    "isAvailable": false
                                }
                            ]
                        }
                    """.trimIndent()
            }
        }

        When("예약을 단건 조회한다.") {
            val response = RestAssured
                .given().log().all()
                .`when`().get(
                    "/api/maps/{mapId}/spaces/{spaceId}/reservations/{reservationId}",
                    campus.id,
                    space1.id,
                    reservation1.id
                )
                .then().log().all()
                .extract()

            Then("200 응답과 조회 결과를 반환한다.") {
                response.statusCode() shouldBe 200
                response.asPrettyJson() shouldBe
                    """
                        {
                            "id": 1,
                            "startDateTime": "2023-11-07T11:00",
                            "endDateTime": "2023-11-07T12:00",
                            "name": "회의실 예약",
                            "description": "회의실 예약 설명"
                        }
                    """.trimIndent()
            }
        }

        When("특정 공간의 모든 예약을 조회한다.") {
            val response = RestAssured
                .given().log().all()
                .param("date", "2023-11-07")
                .`when`().get("/api/maps/{mapId}/spaces/{spaceId}/reservations", campus.id, space1.id)
                .then().log().all()
                .extract()

            Then("200 응답과 조회 결과를 반환한다.") {
                response.statusCode() shouldBe 200
                response.asPrettyJson() shouldBe
                    """
                        {
                            "reservation": [
                                {
                                    "id": 1,
                                    "startDateTime": "2023-11-07T11:00",
                                    "endDateTime": "2023-11-07T12:00",
                                    "name": "회의실 예약",
                                    "description": "회의실 예약 설명"
                                },
                                {
                                    "id": 2,
                                    "startDateTime": "2023-11-07T12:00",
                                    "endDateTime": "2023-11-07T13:00",
                                    "name": "회의실 예약",
                                    "description": "회의실 예약 설명"
                                }
                            ]
                        }
                    """.trimIndent()
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
