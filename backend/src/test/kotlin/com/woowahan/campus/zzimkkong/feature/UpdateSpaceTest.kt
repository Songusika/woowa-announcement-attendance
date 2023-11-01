package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_1
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_2
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_3
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_4
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
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
class UpdateSpaceTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({

    RestAssured.port = port

    Given("회의실 정보를 등록한다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val campusId = CampusFixture.캠퍼스_생성_ID_반환(campus, slackUrl)
        val space = SpaceFixture.랜딩_강의장(campusId)
        val thumbnail = "thumbnail"
        val updatedSpace = SpaceFixture.굿샷_강의장(campusId)
        val updatedThumbnail = "newThumbnail"
        val settings = listOf(회의실_예약_설정_1(), 회의실_예약_설정_2())
        val spaceId = SpaceFixture.회의실_생성_ID_반환(space, thumbnail, settings)

        When("회의실 정보를 수정한다") {
            val updatedSettings = listOf(회의실_예약_설정_3(), 회의실_예약_설정_4())
            val response = SpaceFixture.회의실_단건_수정(
                campusId.toString(),
                spaceId.toString(),
                updatedSpace,
                updatedThumbnail,
                updatedSettings
            )

            Then("200 응답을 반환한다.") {
                response.statusCode() shouldBe 200
            }

            Then("200 응답과 수정된 회의실 정보들을 확인한다.") {
                val updatedResponse = SpaceFixture.회의실_단건_조회(campusId.toString(), spaceId.toString())
                updatedResponse.statusCode() shouldBe 200
                ResponseUtils.getPrettyJson(updatedResponse) shouldBe
                    """
                        {
                            "name": "${updatedSpace.name}",
                            "color": "${updatedSpace.color}",
                            "area": "${updatedSpace.area}",
                            "reservationEnable": ${updatedSpace.reservationEnabled},
                            "settings": [
                                {
                                    "settingStartTime": "${updatedSettings[0].startTime}",
                                    "settingEndTime": "${updatedSettings[0].endTime}",
                                    "reservationMinimumTimeUnit": 1,
                                    "reservationMaximumTimeUnit": ${updatedSettings[0].maximumMinute},
                                    "enabledDayOfWeek": {
                                        "monday": ${updatedSettings[0].getEnableDays().contains(MONDAY)},
                                        "tuesday": ${updatedSettings[0].getEnableDays().contains(TUESDAY)},
                                        "wednesday": ${updatedSettings[0].getEnableDays().contains(WEDNESDAY)},
                                        "thursday": ${updatedSettings[0].getEnableDays().contains(THURSDAY)},
                                        "friday": ${updatedSettings[0].getEnableDays().contains(FRIDAY)},
                                        "saturday": ${updatedSettings[0].getEnableDays().contains(SATURDAY)},
                                        "sunday": ${updatedSettings[0].getEnableDays().contains(SUNDAY)}
                                    }
                                },
                                {
                                    "settingStartTime": "${updatedSettings[1].startTime}",
                                    "settingEndTime": "${updatedSettings[1].endTime}",
                                    "reservationMinimumTimeUnit": 1,
                                    "reservationMaximumTimeUnit": ${updatedSettings[1].maximumMinute},
                                    "enabledDayOfWeek": {
                                        "monday": ${updatedSettings[1].getEnableDays().contains(MONDAY)},
                                        "tuesday": ${updatedSettings[1].getEnableDays().contains(TUESDAY)},
                                        "wednesday": ${updatedSettings[1].getEnableDays().contains(WEDNESDAY)},
                                        "thursday": ${updatedSettings[1].getEnableDays().contains(THURSDAY)},
                                        "friday": ${updatedSettings[1].getEnableDays().contains(FRIDAY)},
                                        "saturday": ${updatedSettings[1].getEnableDays().contains(SATURDAY)},
                                        "sunday": ${updatedSettings[1].getEnableDays().contains(SUNDAY)}
                                    }
                                }
                            ]
                        }
                    """.trimIndent()
            }
        }
    }
})
