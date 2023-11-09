package com.woowahan.campus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class CampusApplication

fun main(args: Array<String>) {

    runApplication<CampusApplication>(*args)
}
