package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.domain.Space
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import openapi.api.FindSpaceApi
import openapi.model.SpaceGetSingle
import openapi.model.SpaceGetSingleSettingsInner
import openapi.model.SpaceGetSingleSettingsInnerEnabledDayOfWeek
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek

@RestController
class ReadSpace(
    val spaceRepository: SpaceRepository,
) : FindSpaceApi {

    override fun findAllSpace(mapId: Int): ResponseEntity<List<SpaceGetSingle>> {
        val spaces = spaceRepository.findAllByCampusId(mapId.toLong())

        return ResponseEntity.ok(
            spaces.map { createSpaceSingleResponse(it) }.toList()
        )
    }

    override fun findSpace(mapId: Int, spaceId: Int): ResponseEntity<SpaceGetSingle> {
        val space = spaceRepository.findById(spaceId.toLong())
            .orElseThrow { IllegalArgumentException() }

        return ResponseEntity.ok(
            createSpaceSingleResponse(space)
        )
    }

    private fun createSpaceSingleResponse(space: Space) = SpaceGetSingle(
        name = space.name,
        color = space.color,
        area = space.area,
        reservationEnable = space.reservationEnabled,
        settings = parseToSettingResponses(space),
    )

    private fun parseToSettingResponses(space: Space) = space.settings.map {
        SpaceGetSingleSettingsInner(
            settingStartTime = it.startTime.toString(),
            settingEndTime = it.endTime.toString(),
            reservationMinimumTimeUnit = 1,
            reservationMaximumTimeUnit = it.maximumMinute,
            enabledDayOfWeek = SpaceGetSingleSettingsInnerEnabledDayOfWeek(
                monday = it.getEnableDays().contains(DayOfWeek.MONDAY),
                tuesday = it.getEnableDays().contains(DayOfWeek.TUESDAY),
                wednesday = it.getEnableDays().contains(DayOfWeek.WEDNESDAY),
                thursday = it.getEnableDays().contains(DayOfWeek.THURSDAY),
                friday = it.getEnableDays().contains(DayOfWeek.FRIDAY),
                saturday = it.getEnableDays().contains(DayOfWeek.SATURDAY),
                sunday = it.getEnableDays().contains(DayOfWeek.SUNDAY),
            )
        )
    }.toList()
}
