package org.fitverse.project.features.user.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ClassType {
    @SerialName("TITAN") TITAN,
    @SerialName("SAGE")  SAGE,
    @SerialName("NOMAD") NOMAD,
}