package com.meally.backend.common.strava.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class StravaActivityDetailsDto(
    val id: Long,
    val name: String,
    @JsonProperty("start_date_local")
    val startDateLocal: Instant,
    val calories: Double,
    @JsonProperty("sport_type")
    val sportType: String,
    @JsonProperty("elapsed_time")
    val elapsedTime: Long,
)

enum class StravaSportType {
    AlpineSki, BackcountrySki, Badminton, Canoeing, Crossfit, EBikeRide, Elliptical, EMountainBikeRide, Golf, GravelRide, Handcycle, HighIntensityIntervalTraining, Hike, IceSkate, InlineSkate, Kayaking, Kitesurf, MountainBikeRide, NordicSki, Pickleball, Pilates, Racquetball, Ride, RockClimbing, RollerSki, Rowing, Run, Sail, Skateboard, Snowboard, Snowshoe, Soccer, Squash, StairStepper, StandUpPaddling, Surfing, Swim, TableTennis, Tennis, TrailRun, Velomobile, VirtualRide, VirtualRow, VirtualRun, Walk, WeightTraining, Wheelchair, Windsurf, Workout, Yoga,
    Unknown;

    companion object {
        fun safeValueOf(value: String) = StravaSportType.entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: Unknown
    }
}
