package com.meally.backend.thirdPartyToken

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ThirdPartyTokenRepository : JpaRepository<ThirdPartyToken, UUID> {

    fun findByUserIdAndProvider(userId: UUID, provider: ThirdPartyTokenProvider): ThirdPartyToken?
}