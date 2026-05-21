package org.fitverse.domain.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ClassType {
    @SerialName("TITAN")   TITAN,
    @SerialName("SAGE")    SAGE,
    @SerialName("NOMAD")   NOMAD,
}