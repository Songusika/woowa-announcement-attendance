package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.DayOfWeeks
import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.SettingRepository
import com.woowahan.campus.zzimkkong.domain.Space
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import openapi.api.UpdateSpaceApi
import openapi.model.SpacePut
import openapi.model.SpacePutSettingsInner
import openapi.model.SpacePutSettingsInnerEnabledDayOfWeek
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime

@RestController
class UpdateSpace(
    val spaceRepository: SpaceRepository,
    val settingRepository: SettingRepository,
    val campusRepository: CampusRepository,
) : UpdateSpaceApi {

    @Transactional
    override fun updateSpace(mapId: Int, spaceId: Int, spacePut: SpacePut): ResponseEntity<Unit> {
        updateThumbnail(mapId, spacePut.thumbnail)
        val findSpace = updateSpace(spaceId, spacePut)
        updateSettings(findSpace, spacePut.settings)

        return ResponseEntity.ok().build()
    }

    private fun updateThumbnail(mapId: Int, thumbnail: String) {
        val campus = campusRepository.findById(mapId.toLong())
            .orElseThrow { IllegalArgumentException() }
        campus.updateThumbnail(thumbnail)
        campusRepository.save(campus)
    }

    private fun updateSpace(spaceId: Int, spacePut: SpacePut): Space {
        val findSpace = spaceRepository.findById(spaceId.toLong())
            .orElseThrow { IllegalArgumentException() }
        findSpace.update(
            name = spacePut.name,
            color = spacePut.color,
            area = spacePut.area,
            reservationEnabled = spacePut.reservationEnable,
        )
        return spaceRepository.save(findSpace)
    }

    private fun updateSettings(findSpace: Space, settings: List<SpacePutSettingsInner>) {
        settingRepository.deleteAllBySpaceId(findSpace.id)

        settingRepository.saveAll(
            settings.map {
                Setting(
                    spaceId = findSpace.id,
                    startTime = LocalTime.parse(it.settingStartTime),
                    endTime = LocalTime.parse(it.settingEndTime),
                    maximumMinute = it.reservationMaximumTimeUnit,
                    enableDays = parseToEnableDays(it.enabledDayOfWeek)
                )
            }.toList()
        )
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
