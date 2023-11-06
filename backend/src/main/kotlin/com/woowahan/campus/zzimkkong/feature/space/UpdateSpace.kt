package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks
import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.Space
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.SpaceSettingsValidator
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.UpdateSpaceApi
import openapi.model.SpacePut
import openapi.model.SpacePutSettingsInnerEnabledDayOfWeek
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime

@RestController
class UpdateSpace(
    val spaceRepository: SpaceRepository,
    val campusRepository: CampusRepository,
) : UpdateSpaceApi {

    @Transactional
    override fun updateSpace(mapId: Int, spaceId: Int, spacePut: SpacePut): ResponseEntity<Unit> {
        updateThumbnail(mapId, spacePut.thumbnail)
        updateSpace(spaceId, spacePut)

        return ResponseEntity.ok().build()
    }

    private fun updateThumbnail(mapId: Int, thumbnail: String) {
        val campus = campusRepository.getById(mapId.toLong())
        campus.updateThumbnail(thumbnail)
        campusRepository.save(campus)
    }

    private fun updateSpace(spaceId: Int, spacePut: SpacePut): Space {
        val findSpace = spaceRepository.getById(spaceId.toLong())

        findSpace.update(
            name = spacePut.name,
            color = spacePut.color,
            area = spacePut.area,
            reservationEnabled = spacePut.reservationEnable,
            settings = spacePut.settings.map {
                Setting(
                    startTime = LocalTime.parse(it.settingStartTime),
                    endTime = LocalTime.parse(it.settingEndTime),
                    maximumMinute = it.reservationMaximumTimeUnit,
                    enableDays = parseToEnableDays(it.enabledDayOfWeek)
                )
            }.toList()
        )

        SpaceSettingsValidator.validate(findSpace)
        return spaceRepository.save(findSpace)
    }

    private fun parseToEnableDays(it: SpacePutSettingsInnerEnabledDayOfWeek): String {
        return DayOfWeeks.values()
            .filter { day ->
                when (day) {
                    DayOfWeeks.MONDAY -> it.monday
                    DayOfWeeks.TUESDAY -> it.tuesday
                    DayOfWeeks.WEDNESDAY -> it.wednesday
                    DayOfWeeks.THURSDAY -> it.thursday
                    DayOfWeeks.FRIDAY -> it.friday
                    DayOfWeeks.SATURDAY -> it.saturday
                    DayOfWeeks.SUNDAY -> it.sunday
                }
            }.joinToString(",") { it.name }
    }
}
