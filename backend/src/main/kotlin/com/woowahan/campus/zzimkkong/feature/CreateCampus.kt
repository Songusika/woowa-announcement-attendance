package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.domain.Campus
import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannel
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import openapi.api.CreateMapApi
import openapi.model.MapPost
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CreateCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : CreateMapApi {

    @Transactional
    override fun createMap(mapPost: MapPost): ResponseEntity<Unit> {
        val savedCampus = campusRepository.save(
            Campus(
                name = mapPost.mapName,
                drawing = mapPost.mapDrawing,
                thumbnail = mapPost.thumbnail
            )
        )
        slackChannelRepository.save(
            SlackChannel(
                url = mapPost.slackUrl,
                campusId = savedCampus.id
            )
        )
        return ResponseEntity.created(URI.create("/api/maps/${savedCampus.id}")).build()
    }
}
