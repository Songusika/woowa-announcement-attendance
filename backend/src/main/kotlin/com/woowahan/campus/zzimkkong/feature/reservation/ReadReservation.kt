package com.woowahan.campus.zzimkkong.feature.reservation

import com.woowahan.campus.zzimkkong.domain.DayOfWeeks
import com.woowahan.campus.zzimkkong.domain.Reservation
import com.woowahan.campus.zzimkkong.domain.ReservationRepository
import com.woowahan.campus.zzimkkong.domain.ReservationValidator
import com.woowahan.campus.zzimkkong.domain.Space
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.FindReservationApi
import openapi.model.ReservationGetSingle
import openapi.model.ReservationsGet
import openapi.model.ReservationsGetReservationInner
import openapi.model.SpaceGetReservationEnabled
import openapi.model.SpaceGetReservationEnabledSpacesInner
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
class ReadReservation(
    val spaceRepository: SpaceRepository,
    val reservationRepository: ReservationRepository,
) : FindReservationApi {

    override fun findReservation(mapId: Int, spaceId: Int, reservationId: Int): ResponseEntity<ReservationGetSingle> {
        val reservation = reservationRepository.getById(reservationId.toLong())
        return ResponseEntity.ok(
            ReservationGetSingle(
                id = reservation.id.toInt(),
                startDateTime = "${reservation.date}T${reservation.startTime}",
                endDateTime = "${reservation.date}T${reservation.endTime}",
                name = reservation.name,
                description = reservation.description
            )
        )
    }

    override fun findReservations(mapId: Int, spaceId: Int, date: LocalDate): ResponseEntity<ReservationsGet> {
        val reservations = reservationRepository.findAllBySpaceIdAndDate(spaceId.toLong(), date)
        val response = reservations.map {
            ReservationsGetReservationInner(
                id = it.id.toInt(),
                startDateTime = "${it.date}T${it.startTime}",
                endDateTime = "${it.date}T${it.endTime}",
                name = it.name,
                description = it.description
            )
        }.toList()

        return ResponseEntity.ok(ReservationsGet(response))
    }

    override fun findSpaceAvailability(
        mapId: Int,
        startDateTime: String,
        endDateTime: String
    ): ResponseEntity<SpaceGetReservationEnabled> {
        val startLocalDateTime = LocalDateTime.parse(startDateTime)
        val endLocalDateTime = LocalDateTime.parse(endDateTime)
        ReservationValidator.validateTime(startLocalDateTime, endLocalDateTime, LocalDateTime.now())
        require(startLocalDateTime < endLocalDateTime) { "시작 시간이 종료 시간보다 늦을 수 없습니다." }

        val spaces = spaceRepository.findAllByCampusId(mapId.toLong())
        val availableSpaces: MutableMap<Long, Boolean> = getAvailableSpaces(spaces, startLocalDateTime, endLocalDateTime)
        updateAvailableSpacesByReservations(availableSpaces, startLocalDateTime, endLocalDateTime)

        return ResponseEntity.ok(
            SpaceGetReservationEnabled(
                mapId = mapId,
                spaces = availableSpaces.map {
                    SpaceGetReservationEnabledSpacesInner(
                        spaceId = it.key.toInt(),
                        isAvailable = it.value
                    )
                }.toList()
            )
        )
    }

    private fun getAvailableSpaces(
        spaces: List<Space>,
        startLocalDateTime: LocalDateTime,
        endLocalDateTime: LocalDateTime
    ) = spaces.associate {
        val isValidSpace = it.reservationEnabled &&
            it.settings.any { setting ->
                setting.getEnableDays()
                    .contains(DayOfWeeks.valueOf(startLocalDateTime.toLocalDate().dayOfWeek.name)) &&
                    setting.isEnableTime(startLocalDateTime.toLocalTime(), endLocalDateTime.toLocalTime())
            }
        it.id to isValidSpace
    }.toMutableMap()

    private fun updateAvailableSpacesByReservations(
        availableSpaces: MutableMap<Long, Boolean>,
        startLocalDateTime: LocalDateTime,
        endLocalDateTime: LocalDateTime
    ) {
        val spaceIds = availableSpaces.filter { it.value }
            .map { it.key }
            .toList()

        val findReservations: Map<Long, List<Reservation>> = reservationRepository.findAllBySpaceIdIn(spaceIds)
            .groupBy { it.spaceId }

        findReservations.forEach { entry ->
            val reservations: List<Reservation> = entry.value
            val enableReservation: Boolean = reservations.none {
                it.isContain(
                    startTime = startLocalDateTime.toLocalTime(),
                    endTime = endLocalDateTime.toLocalTime()
                )
            }
            availableSpaces[entry.key] = enableReservation
        }
    }
}
