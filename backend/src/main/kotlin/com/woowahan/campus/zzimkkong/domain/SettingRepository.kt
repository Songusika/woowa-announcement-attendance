package com.woowahan.campus.zzimkkong.domain

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface SettingRepository : CrudRepository<Setting, Long> {

    fun findSettingsBySpaceId(spaceId: Long): List<Setting>

    @Query(
        """
            delete 
            from Setting s
            where s.spaceId = :spaceId
        """
    )
    @Modifying(flushAutomatically = true)
    fun deleteAllBySpaceId(spaceId: Long)
}
