package com.meally.backend.activity.dto

import com.meally.backend.activity.ActivityEntry

data class ActivityEntriesForDateDto(
    val exercise: List<ActivityEntry>,
    val isAuthorized: Boolean,
)
