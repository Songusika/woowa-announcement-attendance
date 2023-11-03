package com.woowahan.campus.utils

import io.kotest.core.listeners.BeforeContainerListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.isRootTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Import(DatabaseCleaner::class)
@Component
@Transactional
class DatabaseCleanerExtension(private val databaseCleaner: DatabaseCleaner) : BeforeContainerListener {

    override suspend fun beforeContainer(testCase: TestCase) {
        if (!testCase.isRootTest()) return
        withContext(Dispatchers.IO) {
            databaseCleaner.clean()
        }
    }
}
