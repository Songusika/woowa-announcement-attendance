package com.woowahan.campus.announcement.support

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    @CreatedDate
    val createdAt: LocalDateTime? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseRootEntity<T : AbstractAggregateRoot<T>>(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : AbstractAggregateRoot<T>() {
    @CreatedDate
    var createdAt: LocalDateTime? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
