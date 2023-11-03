package com.woowahan.campus.utils

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.test.context.TestComponent
import org.springframework.transaction.annotation.Transactional

@TestComponent
class DatabaseCleaner : InitializingBean {

    @PersistenceContext
    private lateinit var entityManager: EntityManager
    private lateinit var tableNames: List<String>

    override fun afterPropertiesSet() {
        tableNames = entityManager.createNativeQuery("SHOW TABLES")
            .resultList
            .map { (it as Array<*>)[0].toString() }
            .toList()
    }

    @Transactional
    fun clean() {
        executeQuery("SET REFERENTIAL_INTEGRITY FALSE")
        tableNames.forEach { executeQuery("TRUNCATE TABLE ${it} RESTART IDENTITY") }

        executeQuery("SET REFERENTIAL_INTEGRITY TRUE")
    }

    fun executeQuery(query: String) {
        entityManager.createNativeQuery(query).executeUpdate()
    }
}
