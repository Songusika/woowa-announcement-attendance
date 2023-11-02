package com.woowahan.campus.zzimkkong.domain

class SpaceSettingsValidator {

    companion object {
        fun validate(space: Space, settings: List<Setting>) {
            validateReservationEnabled(settings, space)
            validateSettings(settings)
        }

        private fun validateSettings(settings: List<Setting>) {
            for ((index, setting) in settings.withIndex()) {
                val enableDays = setting.getEnableDays()
                settings.subList(index + 1, settings.size).forEach { otherSetting ->
                    validateDuplicatedSetting(enableDays, otherSetting, setting)
                }
            }
        }

        private fun validateReservationEnabled(
            settings: List<Setting>,
            space: Space
        ) {
            when {
                settings.isEmpty() -> {
                    require(!space.reservationEnabled) { "예약이 가능한 공간은 최소 한 개의 예약 설정이 필요합니다." }
                }
            }
        }

        private fun validateDuplicatedSetting(
            enableDays: List<DayOfWeeks>,
            otherSetting: Setting,
            setting: Setting,
        ) {
            if (containsDuplicatedDay(enableDays, otherSetting)) {
                require(
                    setting.startTime.isAfter(otherSetting.endTime) ||
                        setting.startTime == otherSetting.endTime ||
                        setting.endTime.isBefore(otherSetting.startTime) ||
                        setting.endTime == otherSetting.startTime
                ) { "시간이 겹칩니다." }
            }
        }

        private fun containsDuplicatedDay(
            enableDays: List<DayOfWeeks>,
            otherSetting: Setting,
        ) = enableDays.intersect(otherSetting.getEnableDays().toSet()).isNotEmpty()
    }
}
