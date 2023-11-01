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
import openapi.model.SpacePut
import openapi.model.SpacePutSettingsInner
import openapi.model.SpacePutSettingsInnerEnabledDayOfWeek
import org.springframework.http.HttpHeaders
import java.time.DayOfWeek

class SpaceFixture {

    companion object {

        fun 굿샷_강의장(campusId: Long) = Space(
            campusId = campusId,
            name = "굿샷 강의장",
            color = "#00000",
            area = "{ \"id\": \"2\", \"type\" : \"rect\", \"x\": \"10\", \"y\": \"20\", \"width\": \"50\", \"height\": \"70\" }",
            reservationEnabled = false
        )

        fun 랜딩_강의장(campusId: Long) = Space(
            campusId = campusId,
            name = "랜딩 강의장",
            color = "#FFFFFF",
            area = "{ \"id\": \"1\", \"type\" : \"rect\", \"x\": \"10\", \"y\": \"10\", \"width\": \"30\", \"height\": \"30\" }",
            reservationEnabled = true
        )

        fun `회의실_생성`(
            space: Space,
            thumbnail: String,
            settings: List<Setting>,
        ): ExtractableResponse<Response> = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(
                SpacePost(
                    space.name, space.color, space.area, thumbnail, space.reservationEnabled,
                    settings.map { setting ->
                        SpacePostSettingsInner(
                            settingStartTime = setting.startTime.toString(),
                            settingEndTime = setting.endTime.toString(),
                            reservationMinimumTimeUnit = 1,
                            reservationMaximumTimeUnit = setting.maximumMinute,
                            enabledDayOfWeek = getDayOfWeekForPost(setting.getEnableDays()),
                        )
                    }
                )
            )
            .`when`().post("/api/maps/{mapId}/spaces", space.campusId)
            .then().log().all()
            .extract()

        fun `회의실_생성_ID_반환`(
            space: Space,
            thumbnail: String,
            settings: List<Setting>,
        ): Long = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(
                SpacePost(
                    space.name, space.color, space.area, thumbnail, space.reservationEnabled,
                    settings.map { setting ->
                        SpacePostSettingsInner(
                            settingStartTime = setting.startTime.toString(),
                            settingEndTime = setting.endTime.toString(),
                            reservationMinimumTimeUnit = 1,
                            reservationMaximumTimeUnit = setting.maximumMinute,
                            enabledDayOfWeek = getDayOfWeekForPost(setting.getEnableDays()),
                        )
                    }
                )
            )
            .`when`().post("/api/maps/{mapId}/spaces", space.campusId)
            .then().log().all()
            .extract()
            .header(HttpHeaders.LOCATION).split("/").last()
            .toLong()

        private fun getDayOfWeekForPost(enableDays: List<DayOfWeek>): SpacePostSettingsInnerEnabledDayOfWeek {
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

        private fun getDayOfWeekForPut(enableDays: List<DayOfWeek>): SpacePutSettingsInnerEnabledDayOfWeek {
            return SpacePutSettingsInnerEnabledDayOfWeek(
                monday = enableDays.contains(DayOfWeek.MONDAY),
                tuesday = enableDays.contains(DayOfWeek.TUESDAY),
                wednesday = enableDays.contains(DayOfWeek.WEDNESDAY),
                thursday = enableDays.contains(DayOfWeek.THURSDAY),
                friday = enableDays.contains(DayOfWeek.FRIDAY),
                saturday = enableDays.contains(DayOfWeek.SATURDAY),
                sunday = enableDays.contains(DayOfWeek.SUNDAY),
            )
        }

        fun `회의실_단건_조회`(mapId: String, spaceId: String): ExtractableResponse<Response> = RestAssured
            .given().log().all()
            .`when`().get("/api/maps/$mapId/spaces/$spaceId")
            .then().log().all()
            .extract()

        fun `회의실_전체_조회`(mapId: String): ExtractableResponse<Response> = RestAssured
            .given().log().all()
            .`when`().get("/api/maps/$mapId/spaces")
            .then().log().all()
            .extract()

        fun `회의실_단건_삭제`(mapId: String, spaceId: String): ExtractableResponse<Response> = RestAssured
            .given().log().all()
            .`when`().delete("/api/maps/$mapId/spaces/$spaceId")
            .then().log().all()
            .extract()

        fun `회의실_단건_수정`(
            mapId: String,
            spaceId: String,
            space: Space,
            thumbnail: String,
            settings: List<Setting>,
        ): ExtractableResponse<Response> = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(
                SpacePut(
                    name = space.name,
                    color = space.color,
                    area = space.area,
                    thumbnail = thumbnail,
                    reservationEnable = space.reservationEnabled,
                    settings = settings.map {
                        SpacePutSettingsInner(
                            settingStartTime = it.startTime.toString(),
                            settingEndTime = it.endTime.toString(),
                            reservationMinimumTimeUnit = 1,
                            reservationMaximumTimeUnit = it.maximumMinute,
                            getDayOfWeekForPut(it.getEnableDays())
                        )
                    }.toList()
                )
            )
            .`when`().put("/api/maps/$mapId/spaces/$spaceId")
            .then().log().all()
            .extract()
    }
}
