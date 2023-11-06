package com.woowahan.campus.zzimkkong.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class TimeConfig {

    @Bean
    fun clock(): Clock {
        return Clock.systemDefaultZone()
    }
}
