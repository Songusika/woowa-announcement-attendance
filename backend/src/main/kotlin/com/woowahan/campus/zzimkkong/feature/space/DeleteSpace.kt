package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.DeleteSpaceApi
import openapi.model.SpaceDelete
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteSpace(
    val spaceRepository: SpaceRepository,
    val campusRepository: CampusRepository,
) : DeleteSpaceApi {

    @Transactional
    override fun removeSpace(mapId: Int, spaceId: Int, spaceDelete: SpaceDelete): ResponseEntity<Unit> {
        val campus = campusRepository.getById(mapId.toLong())
        val space = spaceRepository.getById(spaceId.toLong())
        spaceRepository.delete(space)
        campus.updateThumbnail(spaceDelete.thumbnail)
        return ResponseEntity.noContent().build()
    }
}
