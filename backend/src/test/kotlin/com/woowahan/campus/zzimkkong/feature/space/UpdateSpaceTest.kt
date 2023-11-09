package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.FRIDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.MONDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.SATURDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.SUNDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.THURSDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.TUESDAY
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks.WEDNESDAY
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.getById
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_1
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_2
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_3
import com.woowahan.campus.zzimkkong.fixture.SettingFixture.Companion.회의실_예약_설정_4
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.SpacePut
import openapi.model.SpacePutSettingsInner
import openapi.model.SpacePutSettingsInnerEnabledDayOfWeek
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateSpaceTest(
    @LocalServerPort
    val port: Int,
    val campusRepository: CampusRepository,
    val spaceRepository: SpaceRepository,
) : BehaviorSpec({

    RestAssured.port = port

    Given("회의실 정보를 등록한다.") {
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        val settings = listOf(회의실_예약_설정_1(), 회의실_예약_설정_2())
        val space = spaceRepository.save(SpaceFixture.랜딩_강의장(campus.id, settings))
        val updatedSpace = SpaceFixture.굿샷_강의장(campus.id, settings)
        val updatedThumbnail = "newThumbnail"

        When("회의실 정보를 수정한다") {
            val updatedSettings = listOf(회의실_예약_설정_3(), 회의실_예약_설정_4())
            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(
                    SpacePut(
                        name = updatedSpace.name,
                        color = updatedSpace.color,
                        area = updatedSpace.area,
                        thumbnail = updatedThumbnail,
                        reservationEnable = updatedSpace.reservationEnabled,
                        settings = updatedSettings.map {
                            SpacePutSettingsInner(
                                settingStartTime = it.startTime.toString(),
                                settingEndTime = it.endTime.toString(),
                                reservationMaximumTimeUnit = it.maximumMinute,
                                getDayOfWeekForPut(it.getEnableDays())
                            )
                        }.toList()
                    )
                )
                .`when`().put("/api/maps/${campus.id}/spaces/${space.id}")
                .then().log().all()
                .extract()

            Then("200 응답을 반환한다.") {
                response.statusCode() shouldBe 200
            }

            Then("수정된 회의실 정보들을 확인한다.") {
                val readSpace = spaceRepository.getById(space.id)
                val readCampus = campusRepository.getById(campus.id)

                assertSoftly {
                    readCampus.thumbnail shouldBe updatedThumbnail
                    readSpace.name shouldBe updatedSpace.name
                    readSpace.color shouldBe updatedSpace.color
                    readSpace.area shouldBe updatedSpace.area
                    readSpace.reservationEnabled shouldBe updatedSpace.reservationEnabled
                }
            }
        }
    }
})

private fun getDayOfWeekForPut(enableDays: List<DayOfWeeks>): SpacePutSettingsInnerEnabledDayOfWeek {
    return SpacePutSettingsInnerEnabledDayOfWeek(
        monday = enableDays.contains(MONDAY),
        tuesday = enableDays.contains(TUESDAY),
        wednesday = enableDays.contains(WEDNESDAY),
        thursday = enableDays.contains(THURSDAY),
        friday = enableDays.contains(FRIDAY),
        saturday = enableDays.contains(SATURDAY),
        sunday = enableDays.contains(SUNDAY),
    )
}
