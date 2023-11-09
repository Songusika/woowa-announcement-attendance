package com.woowahan.campus.zzimkkong.domain

import org.springframework.data.repository.Repository

fun SpaceRepository.getById(id: Long): Space = findById(id) ?: throw IllegalArgumentException()

interface SpaceRepository : Repository<Space, Long> {

    fun save(space: Space): Space

    fun findById(id: Long): Space?

    fun findAllByCampusId(campusId: Long): List<Space>

    fun delete(space: Space)
}
