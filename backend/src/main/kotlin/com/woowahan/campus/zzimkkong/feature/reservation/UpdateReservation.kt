package com.woowahan.campus.zzimkkong.feature.reservation

import com.woowahan.campus.zzimkkong.domain.ReservationRepository
import com.woowahan.campus.zzimkkong.domain.ReservationValidator
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.UpdateReservationApi
import openapi.model.ReservationPut
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class UpdateReservation(
    val spaceRepository: SpaceRepository,
    val reservationRepository: ReservationRepository,
) : UpdateReservationApi {

    @Transactional
    override fun updateReservation(
        mapId: Int,
        spaceId: Int,
        reservationId: Int,
        reservationPut: ReservationPut,
    ): ResponseEntity<Unit> {
        val startDateTime = LocalDateTime.parse(reservationPut.startDateTime)
        val endDateTime = LocalDateTime.parse(reservationPut.endDateTime)
        val date = startDateTime.toLocalDate()
        ReservationValidator.validateTime(startDateTime, endDateTime, LocalDateTime.now())
        val findReservation = reservationRepository.getById(reservationId.toLong())
        findReservation.checkPassword(reservationPut.password)

        val space = spaceRepository.getById(spaceId.toLong())
        val findReservations = reservationRepository.findAllBySpaceIdAndDate(space.id, date)
            .filterNot { it.id == reservationId.toLong() }
        val updateReservation = reservationRepository.save(
            findReservation.update(
                startTime = startDateTime.toLocalTime(),
                endTime = endDateTime.toLocalTime(),
                name = reservationPut.name,
                description = reservationPut.description,
                password = reservationPut.password,
            )
        )

        ReservationValidator.validate(space, updateReservation, findReservations)
        return ResponseEntity.ok().build()
    }
}
