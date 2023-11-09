package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.support.DatabaseInitializer
import com.woowahan.campus.support.asPrettyJson
import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.FRIDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.MONDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.SATURDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.SUNDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.THURSDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.TUESDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.WEDNESDAY
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_1
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_3
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReadSpaceTest(
    @LocalServerPort
    val port: Int,
    val campusRepository: CampusRepository,
    val spaceRepository: SpaceRepository,
    val databaseInitializer: DatabaseInitializer,
) : BehaviorSpec({

    extensions(databaseInitializer)

    RestAssured.port = port

    Given("회의실 정보를 등록한다.") {
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        val settings = listOf(회의실_예약_설정_1(), 회의실_예약_설정_3())
        val space = spaceRepository.save(SpaceFixture.랜딩_강의장(campus.id, settings))

        When("회의실 정보를 모두 조회한다.") {
            val response = RestAssured
                .given().log().all()
                .`when`().get("/api/maps/${campus.id}/spaces")
                .then().log().all()
                .extract()

            Then("200 응답과 저장된 회의실 정보들을 반환한다.") {
                response.statusCode() shouldBe 200
                response.asPrettyJson() shouldBe
                    """
                        {
                            "spaces": [
                                {
                                    "name": "${space.name}",
                                    "color": "${space.color}",
                                    "area": "${space.area}",
                                    "reservationEnable": ${space.reservationEnabled},
                                    "settings": [
                                        {
                                            "settingStartTime": "${settings[0].startTime}",
                                            "settingEndTime": "${settings[0].endTime}",
                                            "reservationMaximumTimeUnit": ${settings[0].maximumMinute},
                                            "enabledDayOfWeek": {
                                                "monday": ${settings[0].getEnableDays().contains(MONDAY)},
                                                "tuesday": ${settings[0].getEnableDays().contains(TUESDAY)},
                                                "wednesday": ${settings[0].getEnableDays().contains(WEDNESDAY)},
                                                "thursday": ${settings[0].getEnableDays().contains(THURSDAY)},
                                                "friday": ${settings[0].getEnableDays().contains(FRIDAY)},
                                                "saturday": ${settings[0].getEnableDays().contains(SATURDAY)},
                                                "sunday": ${settings[0].getEnableDays().contains(SUNDAY)}
                                            }
                                        },
                                        {
                                            "settingStartTime": "${settings[1].startTime}",
                                            "settingEndTime": "${settings[1].endTime}",
                                            "reservationMaximumTimeUnit": ${settings[1].maximumMinute},
                                            "enabledDayOfWeek": {
                                                "monday": ${settings[1].getEnableDays().contains(MONDAY)},
                                                "tuesday": ${settings[1].getEnableDays().contains(TUESDAY)},
                                                "wednesday": ${settings[1].getEnableDays().contains(WEDNESDAY)},
                                                "thursday": ${settings[1].getEnableDays().contains(THURSDAY)},
                                                "friday": ${settings[1].getEnableDays().contains(FRIDAY)},
                                                "saturday": ${settings[1].getEnableDays().contains(SATURDAY)},
                                                "sunday": ${settings[1].getEnableDays().contains(SUNDAY)}
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    """.trimIndent()
            }
        }

        When("회의실 단건 정보를 조회한다.") {
            val response = RestAssured
                .given().log().all()
                .`when`().get("/api/maps/${campus.id}/spaces/${space.id}")
                .then().log().all()
                .extract()

            Then("200 응답과 저장된 회의실 정보들을 반환한다.") {
                response.statusCode() shouldBe 200
                response.asPrettyJson() shouldBe
                    """
                        {
                            "name": "${space.name}",
                            "color": "${space.color}",
                            "area": "${space.area}",
                            "reservationEnable": ${space.reservationEnabled},
                            "settings": [
                                {
                                    "settingStartTime": "${settings[0].startTime}",
                                    "settingEndTime": "${settings[0].endTime}",
                                    "reservationMaximumTimeUnit": ${settings[0].maximumMinute},
                                    "enabledDayOfWeek": {
                                        "monday": ${settings[0].getEnableDays().contains(MONDAY)},
                                        "tuesday": ${settings[0].getEnableDays().contains(TUESDAY)},
                                        "wednesday": ${settings[0].getEnableDays().contains(WEDNESDAY)},
                                        "thursday": ${settings[0].getEnableDays().contains(THURSDAY)},
                                        "friday": ${settings[0].getEnableDays().contains(FRIDAY)},
                                        "saturday": ${settings[0].getEnableDays().contains(SATURDAY)},
                                        "sunday": ${settings[0].getEnableDays().contains(SUNDAY)}
                                    }
                                },
                                {
                                    "settingStartTime": "${settings[1].startTime}",
                                    "settingEndTime": "${settings[1].endTime}",
                                    "reservationMaximumTimeUnit": ${settings[1].maximumMinute},
                                    "enabledDayOfWeek": {
                                        "monday": ${settings[1].getEnableDays().contains(MONDAY)},
                                        "tuesday": ${settings[1].getEnableDays().contains(TUESDAY)},
                                        "wednesday": ${settings[1].getEnableDays().contains(WEDNESDAY)},
                                        "thursday": ${settings[1].getEnableDays().contains(THURSDAY)},
                                        "friday": ${settings[1].getEnableDays().contains(FRIDAY)},
                                        "saturday": ${settings[1].getEnableDays().contains(SATURDAY)},
                                        "sunday": ${settings[1].getEnableDays().contains(SUNDAY)}
                                    }
                                }
                            ]
                        }
                    """.trimIndent()
            }
        }
    }
})
