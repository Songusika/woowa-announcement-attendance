package com.woowahan.campus.zzimkkong.fixture

import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.Space
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import openapi.model.SpacePost
import openapi.model.SpacePostSettingsInner
import openapi.model.SpacePostSettingsInnerEnabledDayOfWeek
import java.time.DayOfWeek
import java.time.LocalTime

class SpaceFixture {

    companion object {

        fun 랜딩_강의장(campusId: Long, settings: List<Setting>) = Space(
            campusId = campusId,
            name = "랜딩 강의장",
            color = "#FFFFFF",
            area = "{ \"id\": \"1\", \"type\" : \"rect\", \"x\": \"10\", \"y\": \"10\", \"width\": \"30\", \"height\": \"30\" }",
            reservationEnabled = true,
            settings = settings
        )

        fun 랜딩_강의장(campusId: Long) = Space(
            campusId = campusId,
            name = "랜딩 강의장",
            color = "#FFFFFF",
            area = "{ \"id\": \"1\", \"type\" : \"rect\", \"x\": \"10\", \"y\": \"10\", \"width\": \"30\", \"height\": \"30\" }",
            reservationEnabled = true,
            settings = listOf(
                SettingFixture.회의실_예약_설정_1(),
                SettingFixture.회의실_예약_설정_2()
            )
        )

        fun `회의실_생성`(
            space: Space,
            thumbnail: String,
        ): ExtractableResponse<Response> = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(
                SpacePost(
                    space.name, space.color, space.area, thumbnail, space.reservationEnabled,
                    space.settings.map { setting ->
                        SpacePostSettingsInner(
                            settingStartTime = setting.startTime.toString(),
                            settingEndTime = setting.endTime.toString(),
                            reservationMinimumTimeUnit = 1,
                            reservationMaximumTimeUnit = setting.maximumMinute,
                            enabledDayOfWeek = getDayOfWeek(setting.getEnableDays()),
                        )
                    }
                )
            )
            .`when`().post("/api/maps/{mapId}/spaces", space.campusId)
            .then().log().all()
            .extract()

        private fun getDayOfWeek(enableDays: List<DayOfWeek>): SpacePostSettingsInnerEnabledDayOfWeek {
            return SpacePostSettingsInnerEnabledDayOfWeek(
                monday = enableDays.contains(DayOfWeek.MONDAY),
                tuesday = enableDays.contains(DayOfWeek.TUESDAY),
                wednesday = enableDays.contains(DayOfWeek.WEDNESDAY),
                thursday = enableDays.contains(DayOfWeek.THURSDAY),
                friday = enableDays.contains(DayOfWeek.FRIDAY),
                saturday = enableDays.contains(DayOfWeek.SATURDAY),
                sunday = enableDays.contains(DayOfWeek.SUNDAY),
            )
        }
    }
}

class SettingFixture {

    companion object {

        fun 회의실_예약_설정_1() = Setting(
            startTime = LocalTime.of(10, 0, 0),
            endTime = LocalTime.of(11, 0, 0),
            maximumMinute = 60,
            enableDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY",
        )

        fun 회의실_예약_설정_2() = Setting(
            startTime = LocalTime.of(11, 0, 0),
            endTime = LocalTime.of(12, 0, 0),
            maximumMinute = 60,
            enableDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY",
        )
    }
}
