package com.example.remote.dto.activePlans

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivatePlanDto(

    @SerialName("plan_id") val planId: String,
)