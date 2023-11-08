package com.woowahan.campus.zzimkkong.fixture

import com.woowahan.campus.zzimkkong.domain.Reservation
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import openapi.model.ReservationPost
import java.time.LocalDate
import java.time.LocalTime

class ReservationFixture {

    companion object {

        fun 회의실_예약(
            date: String,
            startTime: String,
            endTime: String,
        ): Reservation {
            return 회의실_예약(0L, date, startTime, endTime)
        }

        fun 회의실_예약(
            spaceId: Long,
            date: String,
            startTime: String,
            endTime: String,
        ): Reservation {
            return 회의실_예약(spaceId, date, startTime, endTime, "1234")
        }

        fun 회의실_예약(
            spaceId: Long,
            date: String,
            startTime: String,
            endTime: String,
            password: String,
        ): Reservation {
            return Reservation(
                spaceId = spaceId,
                date = LocalDate.parse(date),
                startTime = LocalTime.parse(startTime),
                endTime = LocalTime.parse(endTime),
                name = "회의실 예약",
                description = "회의실 예약 설명",
                password = password
            )
        }

        fun 예약_생성(mapId: Long, spaceId: Long, reservation: Reservation): ExtractableResponse<Response> {
            return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(
                    ReservationPost(
                        "${reservation.date}T${reservation.startTime}",
                        "${reservation.date}T${reservation.endTime}",
                        reservation.name,
                        reservation.description,
                        reservation.password
                    )
                )
                .`when`().post("/api/maps/{mapId}/spaces/{spaceId}/reservations", mapId, spaceId)
                .then().log().all()
                .extract()
        }
    }
}
