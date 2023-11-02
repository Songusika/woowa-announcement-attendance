package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SettingRepository
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import openapi.api.DeleteSpaceApi
import openapi.model.SpaceDelete
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteSpace(
    val spaceRepository: SpaceRepository,
    val settingRepository: SettingRepository,
    val campusRepository: CampusRepository,
) : DeleteSpaceApi {

    @Transactional
    override fun removeSpace(mapId: Int, spaceId: Int, spaceDelete: SpaceDelete): ResponseEntity<Unit> {
        val campus = campusRepository.findById(mapId.toLong())
            .orElseThrow { IllegalArgumentException() }
        spaceRepository.findById(spaceId.toLong())
            .orElseThrow { IllegalArgumentException() }
        settingRepository.deleteAllBySpaceId(spaceId.toLong())
        spaceRepository.deleteById(spaceId.toLong())
        campus.updateThumbnail(spaceDelete.thumbnail)
        return ResponseEntity.noContent().build()
    }
}
