package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.Setting
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
                settings = spacePost.settings.map {
                    Setting(
                        startTime = LocalTime.parse(it.settingStartTime),
                        endTime = LocalTime.parse(it.settingEndTime),
                        maximumMinute = it.reservationMaximumTimeUnit,
                        enableDays = parseToEnableDays(it.enabledDayOfWeek)
                    )
                }
            )
        )

        campus.updateThumbnail(spacePost.thumbnail)
        campusRepository.save(campus)

        return ResponseEntity.created(URI.create("/api/maps/${campus.id}/spaces/${space.id}")).build()
    }

    private fun parseToEnableDays(it: SpacePostSettingsInnerEnabledDayOfWeek): String {
        val enabledDayOfWeek = mutableListOf<String>()
        if (it.monday) {
            enabledDayOfWeek.add(DayOfWeek.MONDAY.name)
        }
        if (it.tuesday) {
            enabledDayOfWeek.add(DayOfWeek.TUESDAY.name)
        }
        if (it.wednesday) {
            enabledDayOfWeek.add(DayOfWeek.WEDNESDAY.name)
        }
        if (it.thursday) {
            enabledDayOfWeek.add(DayOfWeek.THURSDAY.name)
        }
        if (it.friday) {
            enabledDayOfWeek.add(DayOfWeek.FRIDAY.name)
        }
        if (it.saturday) {
            enabledDayOfWeek.add(DayOfWeek.SATURDAY.name)
        }
        if (it.sunday) {
            enabledDayOfWeek.add(DayOfWeek.SUNDAY.name)
        }
        return enabledDayOfWeek.joinToString(",")
    }
}
