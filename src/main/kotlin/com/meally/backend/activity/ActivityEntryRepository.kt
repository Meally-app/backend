package com.meally.backend.activity

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface ActivityEntryRepository : JpaRepository<ActivityEntry, UUID> {

    fun findAllByUserIdAndDate(userId: UUID, date: LocalDate): List<ActivityEntry>

    fun findAllByUserIdAndDateAfterAndDateBefore(userId: UUID, after: LocalDate, before: LocalDate): List<ActivityEntry>


    fun findAllByUserId(userId: UUID): List<ActivityEntry>
}