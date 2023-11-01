package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import com.woowahan.campus.zzimkkong.support.DatabaseInitializer
import com.woowahan.campus.zzimkkong.support.ResponseUtils
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReadSpaceTest(
    @LocalServerPort
    val port: Int,
    val databaseInitializer: DatabaseInitializer
) : BehaviorSpec({

    extensions(databaseInitializer)

    RestAssured.port = port

    Given("회의실 정보를 등록한다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val campusId = CampusFixture.캠퍼스_생성_ID_반환(campus, slackUrl)
        val space = SpaceFixture.랜딩_강의장(campusId)
        val thumbnail = "thumbnail"
        val spaceId = SpaceFixture.회의실_생성_ID_반환(space, thumbnail)

        When("회의실 정보를 모두 조회한다.") {
            val response = SpaceFixture.회의실_전체_조회(campusId.toString())

            Then("200 응답과 저장된 회의실 정보들을 반환한다.") {
                response.statusCode() shouldBe 200
                ResponseUtils.getPrettyJson(response) shouldBe
                    """
                        [
                            {
                                "name": "${space.name}",
                                "color": "${space.color}",
                                "area": "${space.area}",
                                "reservationEnable": ${space.reservationEnabled},
                                "settings": [
                                    {
                                        "settingStartTime": "${space.settings[0].startTime}",
                                        "settingEndTime": "${space.settings[0].endTime}",
                                        "reservationMinimumTimeUnit": 1,
                                        "reservationMaximumTimeUnit": ${space.settings[0].maximumMinute},
                                        "enabledDayOfWeek": {
                                            "monday": ${space.settings[0].getEnableDays().contains(MONDAY)},
                                            "tuesday": ${space.settings[0].getEnableDays().contains(TUESDAY)},
                                            "wednesday": ${space.settings[0].getEnableDays().contains(WEDNESDAY)},
                                            "thursday": ${space.settings[0].getEnableDays().contains(THURSDAY)},
                                            "friday": ${space.settings[0].getEnableDays().contains(FRIDAY)},
                                            "saturday": ${space.settings[0].getEnableDays().contains(SATURDAY)},
                                            "sunday": ${space.settings[0].getEnableDays().contains(SUNDAY)}
                                        }
                                    },
                                    {
                                        "settingStartTime": "${space.settings[1].startTime}",
                                        "settingEndTime": "${space.settings[1].endTime}",
                                        "reservationMinimumTimeUnit": 1,
                                        "reservationMaximumTimeUnit": ${space.settings[1].maximumMinute},
                                        "enabledDayOfWeek": {
                                            "monday": ${space.settings[1].getEnableDays().contains(MONDAY)},
                                            "tuesday": ${space.settings[1].getEnableDays().contains(TUESDAY)},
                                            "wednesday": ${space.settings[1].getEnableDays().contains(WEDNESDAY)},
                                            "thursday": ${space.settings[1].getEnableDays().contains(THURSDAY)},
                                            "friday": ${space.settings[1].getEnableDays().contains(FRIDAY)},
                                            "saturday": ${space.settings[1].getEnableDays().contains(SATURDAY)},
                                            "sunday": ${space.settings[1].getEnableDays().contains(SUNDAY)}
                                        }
                                    }
                                ]
                            }
                        ]
                    """.trimIndent()
            }
        }

        When("회의실 단건 정보를 조회한다.") {
            val response = SpaceFixture.회의실_단건_조회(campusId.toString(), spaceId.toString())

            Then("200 응답과 저장된 회의실 정보들을 반환한다.") {
                response.statusCode() shouldBe 200
                ResponseUtils.getPrettyJson(response) shouldBe
                    """
                        {
                            "name": "${space.name}",
                            "color": "${space.color}",
                            "area": "${space.area}",
                            "reservationEnable": ${space.reservationEnabled},
                            "settings": [
                                {
                                    "settingStartTime": "${space.settings[0].startTime}",
                                    "settingEndTime": "${space.settings[0].endTime}",
                                    "reservationMinimumTimeUnit": 1,
                                    "reservationMaximumTimeUnit": ${space.settings[0].maximumMinute},
                                    "enabledDayOfWeek": {
                                        "monday": ${space.settings[0].getEnableDays().contains(MONDAY)},
                                        "tuesday": ${space.settings[0].getEnableDays().contains(TUESDAY)},
                                        "wednesday": ${space.settings[0].getEnableDays().contains(WEDNESDAY)},
                                        "thursday": ${space.settings[0].getEnableDays().contains(THURSDAY)},
                                        "friday": ${space.settings[0].getEnableDays().contains(FRIDAY)},
                                        "saturday": ${space.settings[0].getEnableDays().contains(SATURDAY)},
                                        "sunday": ${space.settings[0].getEnableDays().contains(SUNDAY)}
                                    }
                                },
                                {
                                    "settingStartTime": "${space.settings[1].startTime}",
                                    "settingEndTime": "${space.settings[1].endTime}",
                                    "reservationMinimumTimeUnit": 1,
                                    "reservationMaximumTimeUnit": ${space.settings[1].maximumMinute},
                                    "enabledDayOfWeek": {
                                        "monday": ${space.settings[1].getEnableDays().contains(MONDAY)},
                                        "tuesday": ${space.settings[1].getEnableDays().contains(TUESDAY)},
                                        "wednesday": ${space.settings[1].getEnableDays().contains(WEDNESDAY)},
                                        "thursday": ${space.settings[1].getEnableDays().contains(THURSDAY)},
                                        "friday": ${space.settings[1].getEnableDays().contains(FRIDAY)},
                                        "saturday": ${space.settings[1].getEnableDays().contains(SATURDAY)},
                                        "sunday": ${space.settings[1].getEnableDays().contains(SUNDAY)}
                                    }
                                }
                            ]
                        }
                    """.trimIndent()
            }
        }
    }
})
