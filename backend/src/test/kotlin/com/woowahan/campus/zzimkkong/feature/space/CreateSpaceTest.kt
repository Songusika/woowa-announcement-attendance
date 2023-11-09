package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks
import com.woowahan.campus.zzimkkong.domain.getById
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SettingFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.SpacePost
import openapi.model.SpacePostSettingsInner
import openapi.model.SpacePostSettingsInnerEnabledDayOfWeek
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateSpaceTest(
    @LocalServerPort
    val port: Int,
    val campusRepository: CampusRepository,
) : BehaviorSpec({

    RestAssured.port = port

    Given("이름, 색상, 공간 좌표 정보, 예약 가능 여부, 예약 설정을 받는다.") {
        val slackUrl = "https://slackexample.com"
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        val settings = listOf(SettingFixture.회의실_예약_설정_1())
        val space = SpaceFixture.랜딩_강의장(campus.id, settings)
        val thumbnail = "thumbnail"

        When("공간 정보를 저장한다.") {
            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(
                    SpacePost(
                        space.name, space.color, space.area, thumbnail, space.reservationEnabled,
                        settings.map { setting ->
                            SpacePostSettingsInner(
                                settingStartTime = setting.startTime.toString(),
                                settingEndTime = setting.endTime.toString(),
                                reservationMaximumTimeUnit = setting.maximumMinute,
                                enabledDayOfWeek = getDayOfWeekForPost(setting.getEnableDays()),
                            )
                        }
                    )
                )
                .`when`().post("/api/maps/{mapId}/spaces", space.campusId)
                .then().log().all()
                .extract()

            Then("201 응답과 저장된 공간의 URI를 Location Header로 반환한다.") {
                response.statusCode() shouldBe 201
                response.header(HttpHeaders.LOCATION).shouldNotBeNull()
            }

            Then("캠퍼스의 썸네일 정보가 업데이트된다.") {
                val campusResponse = campusRepository.getById(campus.id)
                campusResponse.thumbnail shouldBe thumbnail
            }
        }
    }
})

private fun getDayOfWeekForPost(enableDays: List<DayOfWeeks>): SpacePostSettingsInnerEnabledDayOfWeek {
    return SpacePostSettingsInnerEnabledDayOfWeek(
        monday = enableDays.contains(DayOfWeeks.MONDAY),
        tuesday = enableDays.contains(DayOfWeeks.TUESDAY),
        wednesday = enableDays.contains(DayOfWeeks.WEDNESDAY),
        thursday = enableDays.contains(DayOfWeeks.THURSDAY),
        friday = enableDays.contains(DayOfWeeks.FRIDAY),
        saturday = enableDays.contains(DayOfWeeks.SATURDAY),
        sunday = enableDays.contains(DayOfWeeks.SUNDAY),
    )
}
