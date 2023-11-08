package com.woowahan.campus.zzimkkong.feature.reservation

import com.woowahan.campus.zzimkkong.domain.ReservationRepository
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.DeleteReservationApi
import openapi.model.RemoveReservationRequest
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteReservation(
    val reservationRepository: ReservationRepository
) : DeleteReservationApi {

    @Transactional
    override fun removeReservation(
        mapId: Int,
        spaceId: Int,
        reservationId: Int,
        removeReservationRequest: RemoveReservationRequest
    ): ResponseEntity<Unit> {
        val reservation = reservationRepository.getById(reservationId.toLong())
        reservation.checkPassword(removeReservationRequest.password)
        reservationRepository.delete(reservation.publishRemovedEvent())
        return ResponseEntity.noContent().build()
    }
}
