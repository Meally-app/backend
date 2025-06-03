package com.meally.backend.common.strava

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
@JsonIgnoreProperties(ignoreUnknown = true)
data class StravaAuthData(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("expires_at")
    val expiresAt: Long,
)
