package com.woowahan.campus.zzimkkong.domain

import org.springframework.data.repository.Repository

fun CampusRepository.getById(id: Long): Campus = findById(id) ?: throw IllegalArgumentException()
fun CampusRepository.getByName(name: String): Campus = findByName(name) ?: throw IllegalArgumentException()

interface CampusRepository : Repository<Campus, Long> {

    fun save(campus: Campus): Campus

    fun findById(id: Long): Campus?

    fun findByName(name: String): Campus?

    fun findAll(): List<Campus>

    fun delete(campus: Campus)

    fun deleteById(id: Long)
}
