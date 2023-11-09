package com.woowahan.campus.zzimkkong.domain

import com.slack.api.Slack
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ZzimkkongSlackMessageSender(
    @Value("\${slack.user.token}")
    private val userToken: String,
    val slackChannelRepository: SlackChannelRepository,
    val campusRepository: CampusRepository,
    val spaceRepository: SpaceRepository,
    val reservationRepository: ReservationRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val slack = Slack.getInstance().methods()

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendCreatedMessage(event: CreatedReservationEvent) {
        val reservation = reservationRepository.getById(event.reservation.id)
        val space = spaceRepository.getById(reservation.spaceId)
        val campus = campusRepository.getById(space.campusId)
        val slackChannel = slackChannelRepository.getByCampusId(campus.id)

        runCatching {
            slack.chatPostMessage {
                it.token(userToken)
                    .channel(slackChannel.url)
                    .text("🎉 예약 생성 알림 🎉")
            }
        }.onFailure {
            log.error("Slack Send Error: {}", it.message, it)
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendUpdatedMessage(event: UpdatedReservationEvent) {
        val reservation = reservationRepository.getById(event.reservation.id)
        val space = spaceRepository.getById(reservation.spaceId)
        val campus = campusRepository.getById(space.campusId)
        val slackChannel = slackChannelRepository.getByCampusId(campus.id)

        runCatching {
            slack.chatPostMessage {
                it.token(userToken)
                    .channel(slackChannel.url)
                    .text("🎉 예약 수정 알림 🎉")
            }
        }.onFailure {
            log.error("Slack Send Error: {}", it.message, it)
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendDeletedMessage(event: DeletedReservationEvent) {
        val reservation = reservationRepository.getById(event.reservation.id)
        val space = spaceRepository.getById(reservation.spaceId)
        val campus = campusRepository.getById(space.campusId)
        val slackChannel = slackChannelRepository.getByCampusId(campus.id)

        runCatching {
            slack.chatPostMessage {
                it.token(userToken)
                    .channel(slackChannel.url)
                    .text("🎉 예약 삭제 알림 🎉")
            }
        }.onFailure {
            log.error("Slack Send Error: {}", it.message, it)
        }
    }
}
