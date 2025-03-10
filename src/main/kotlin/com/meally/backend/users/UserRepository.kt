package com.meally.backend.users

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : CrudRepository<User, UUID> {
    fun findByExternalId(externalId: String): User?
}