package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.Setting
import com.woowahan.campus.zzimkkong.domain.SettingRepository
import com.woowahan.campus.zzimkkong.domain.Space
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import openapi.api.CreateSpaceApi
import openapi.model.SpacePost
import openapi.model.SpacePostSettingsInnerEnabledDayOfWeek
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.DayOfWeek
import java.time.LocalTime

@RestController
class CreateSpace(
    val campusRepository: CampusRepository,
    val spaceRepository: SpaceRepository,
    val settingRepository: SettingRepository,
) : CreateSpaceApi {

    @Transactional
    override fun createSpace(mapId: Int, spacePost: SpacePost): ResponseEntity<Unit> {
        val campus = campusRepository.findById(mapId.toLong())
            .orElseThrow { IllegalArgumentException() }

        val space = spaceRepository.save(
            Space(
                campusId = campus.id,
                name = spacePost.name,
                color = spacePost.color,
                area = spacePost.area,
                reservationEnabled = spacePost.reservationEnable,
            )
        )
        settingRepository.saveAll(
            spacePost.settings.map {
                Setting(
                    spaceId = space.id,
                    startTime = LocalTime.parse(it.settingStartTime),
                    endTime = LocalTime.parse(it.settingEndTime),
                    maximumMinute = it.reservationMaximumTimeUnit,
                    enableDays = parseToEnableDays(it.enabledDayOfWeek)
                )
            }.toList()
        )

        campus.updateThumbnail(spacePost.thumbnail)
        campusRepository.save(campus)

        return ResponseEntity.created(URI.create("/api/maps/${campus.id}/spaces/${space.id}")).build()
    }

    private fun parseToEnableDays(it: SpacePostSettingsInnerEnabledDayOfWeek): String {
        return DayOfWeek.values()
            .filter { day ->
                when (day) {
                    DayOfWeek.MONDAY -> it.monday
                    DayOfWeek.TUESDAY -> it.tuesday
                    DayOfWeek.WEDNESDAY -> it.wednesday
                    DayOfWeek.THURSDAY -> it.thursday
                    DayOfWeek.FRIDAY -> it.friday
                    DayOfWeek.SATURDAY -> it.saturday
                    DayOfWeek.SUNDAY -> it.sunday
                }
            }.joinToString(",") { it.name }
    }
}