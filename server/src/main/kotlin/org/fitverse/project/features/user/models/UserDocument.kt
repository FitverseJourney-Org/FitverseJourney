package org.fitverse.project.features.user.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDocument(
    val uid             : String  = "",
    val name            : String  = "",
    val email           : String  = "",
    val lastname        : String  = "",
    val username        : String  = "",
    val birthDate       : String  = "",
    val gender          : String  = "",
    val classType       : String  = "",
    val height          : Int     = 0,
    val weight          : Double  = 0.0,
    val experienceLevel : String  = "",
    val goals           : String  = "",
    var premium         : Boolean = false,
    val targetWeight    : Double? = null,
    val targetCalories  : Int?    = null,
    val targetProtein   : Double? = null,
    val targetCarbs     : Double? = null,
    val targetFat       : Double? = null,
    val createdAt       : Long    = 0L,
    val updatedAt       : Long    = 0L,
)