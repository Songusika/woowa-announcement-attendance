package com.woowahan.campus.zzimkkong.feature.reservation

import com.woowahan.campus.zzimkkong.domain.Reservation
import com.woowahan.campus.zzimkkong.domain.ReservationRepository
import com.woowahan.campus.zzimkkong.domain.ReservationValidator
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.CreateReservationApi
import openapi.model.ReservationPost
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.time.Clock
import java.time.LocalDateTime

@RestController
class CreateReservation(
    val reservationRepository: ReservationRepository,
    val spaceRepository: SpaceRepository,
    val clock: Clock,
) : CreateReservationApi {

    @Transactional
    override fun createReservation(mapId: Int, spaceId: Int, reservationPost: ReservationPost): ResponseEntity<Unit> {
        val space = spaceRepository.getById(spaceId.toLong())
        val startDateTime = LocalDateTime.parse(reservationPost.startDateTime)
        val endDateTime = LocalDateTime.parse(reservationPost.endDateTime)
        val date = startDateTime.toLocalDate()
        ReservationValidator.validateTime(startDateTime, endDateTime, LocalDateTime.now())

        val reservation = reservationRepository.save(
            Reservation(
                spaceId = space.id,
                date = date,
                startTime = startDateTime.toLocalTime(),
                endTime = endDateTime.toLocalTime(),
                name = reservationPost.name,
                description = reservationPost.description,
                password = reservationPost.password,
            )
        )

        val findReservations = reservationRepository.findAllBySpaceIdAndDate(space.id, date)
        ReservationValidator.validate(space, reservation, findReservations)
        return ResponseEntity.ok().build()
    }
}
