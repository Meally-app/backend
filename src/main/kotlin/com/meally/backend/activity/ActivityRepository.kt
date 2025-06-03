package com.meally.backend.activity

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ActivityRepository : JpaRepository<Activity, UUID>