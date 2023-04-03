package ru.techtheist.multithreadinginvestigation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface AccountRepository : JpaRepository<Account, Long> {
    @Query("select id, version, balance from account where id = ?1 FOR UPDATE", nativeQuery = true)
    fun findByIdForUpdate(id: Long): Optional<Account>
}