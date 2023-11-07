package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.FRIDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.MONDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.SATURDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.SUNDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.THURSDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.TUESDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.WEDNESDAY
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_1
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_3
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import com.woowahan.campus.zzimkkong.support.DatabaseInitializer
import com.woowahan.campus.zzimkkong.support.ResponseUtils
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReadSpaceTest(
    @LocalServerPort
    val port: Int,
    val databaseInitializer: DatabaseInitializer,
) : BehaviorSpec({

    extensions(databaseInitializer)

    RestAssured.port = port

    Given("회의실 정보를 등록한다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val campusId = CampusFixture.캠퍼스_생성_ID_반환(campus, slackUrl)
        val settings = listOf(회의실_예약_설정_1(), 회의실_예약_설정_3())
        val space = SpaceFixture.랜딩_강의장(campusId, settings)
        val thumbnail = "thumbnail"
        val spaceId = SpaceFixture.회의실_생성_ID_반환(space, thumbnail, settings)

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
