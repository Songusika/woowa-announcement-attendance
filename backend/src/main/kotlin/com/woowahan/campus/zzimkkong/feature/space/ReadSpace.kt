package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.DayOfWeeks
import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.Space
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.FindSpaceApi
import openapi.model.SpaceGetAll
import openapi.model.SpaceGetSingle
import openapi.model.SpaceGetSingleSettingsInner
import openapi.model.SpaceGetSingleSettingsInnerEnabledDayOfWeek
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReadSpace(
    val spaceRepository: SpaceRepository,
) : FindSpaceApi {

    override fun findAllSpace(mapId: Int): ResponseEntity<SpaceGetAll> {
        val spaces = spaceRepository.findAllByCampusId(mapId.toLong())
        val response = spaces.map {
            createSpaceSingleResponse(it)
        }.toList()

        return ResponseEntity.ok(SpaceGetAll(response))
    }

    override fun findSpace(mapId: Int, spaceId: Int): ResponseEntity<SpaceGetSingle> {
        val space = spaceRepository.getById(spaceId.toLong())

        return ResponseEntity.ok(createSpaceSingleResponse(space))
    }

    private fun createSpaceSingleResponse(space: Space) = SpaceGetSingle(
        id = space.id.toInt(),
        name = space.name,
        color = space.color,
        area = space.area,
        reservationEnable = space.reservationEnabled,
        settings = parseToSettingResponses(space.settings),
    )

    private fun parseToSettingResponses(settings: List<Setting>): List<SpaceGetSingleSettingsInner> = settings.map {
        SpaceGetSingleSettingsInner(
            settingStartTime = it.startTime.toString(),
            settingEndTime = it.endTime.toString(),
            reservationMaximumTimeUnit = it.maximumMinute,
            enabledDayOfWeek = SpaceGetSingleSettingsInnerEnabledDayOfWeek(
                monday = it.getEnableDays().contains(DayOfWeeks.MONDAY),
                tuesday = it.getEnableDays().contains(DayOfWeeks.TUESDAY),
                wednesday = it.getEnableDays().contains(DayOfWeeks.WEDNESDAY),
                thursday = it.getEnableDays().contains(DayOfWeeks.THURSDAY),
                friday = it.getEnableDays().contains(DayOfWeeks.FRIDAY),
                saturday = it.getEnableDays().contains(DayOfWeeks.SATURDAY),
                sunday = it.getEnableDays().contains(DayOfWeeks.SUNDAY),
            )
        )
    }.toList()
}
