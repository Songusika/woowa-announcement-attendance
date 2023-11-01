package com.woowahan.campus.zzimkkong.domain

import org.springframework.data.repository.CrudRepository

interface SpaceRepository : CrudRepository<Space, Long> {

    fun findAllByCampusId(toLong: Long): List<Space>
}
