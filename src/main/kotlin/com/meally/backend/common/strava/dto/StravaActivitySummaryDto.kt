package com.meally.backend.common.strava.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class StravaActivitySummaryDto(
    val id: Long,
    val name: String,
    @JsonProperty("start_date_local")
    val startDateLocal: Instant,
)
