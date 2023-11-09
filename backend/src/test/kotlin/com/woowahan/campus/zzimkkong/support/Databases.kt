package com.woowahan.campus.zzimkkong.support

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
data class DatabaseInitializationProperties(val excludedTableNames: List<String> = listOf("flyway_schema_history"))

interface Database {
    fun retrieveTables(): List<String>
    fun clear(tableNames: List<String>)
}

abstract class AbstractDatabase(
    private val entityManager: EntityManager,
    private val properties: DatabaseInitializationProperties
) : Database {
    override fun retrieveTables(): List<String> {
        return entityManager.createNativeQuery(metaTablesSql).resultList
            .map { it.toString() }
            .excluded()
    }

    @Transactional
    override fun clear(tableNames: List<String>) {
        entityManager.createNativeQuery(constraintsOffSql).executeUpdate()
        tableNames
            .excluded()
            .forEach { entityManager.createNativeQuery(createTruncateTableSql(it)).executeUpdate() }
        entityManager.createNativeQuery(constraintsOnSql).executeUpdate()
    }

    private fun List<String>.excluded(): List<String> {
        return filterNot { it in properties.excludedTableNames }
    }

    abstract val metaTablesSql: String
    abstract val constraintsOffSql: String
    abstract val constraintsOnSql: String
    abstract fun createTruncateTableSql(tableName: String): String
}

@Component
class H2(
    entityManager: EntityManager,
    properties: DatabaseInitializationProperties
) : AbstractDatabase(entityManager, properties) {
    override val metaTablesSql: String =
        "select table_name from information_schema.tables where table_schema = 'PUBLIC'"

    override val constraintsOffSql: String = "set referential_integrity false"
    override val constraintsOnSql: String = "set referential_integrity true"
    override fun createTruncateTableSql(tableName: String): String = "truncate table $tableName restart identity"
}
