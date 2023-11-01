package com.woowahan.campus.zzimkkong.support

import io.kotest.core.listeners.BeforeContainerListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.isRootTest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DatabaseInitializer(
        private val database: Database
) : BeforeContainerListener {
    override suspend fun beforeContainer(testCase: TestCase) {
        if (!testCase.isRootTest()) return
        println("DB 초기화를 시작합니다. beforeSpec")
        cleanUp()
    }

    private fun cleanUp() {
        database.clear(database.retrieveTables())
    }
}
