package org.fitverse.project.features.user.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDocument(
    @SerialName("uid")              val uid             : String  = "",
    @SerialName("name")             val name            : String  = "",
    @SerialName("email")            val email           : String  = "",
    @SerialName("lastname")         val lastname        : String  = "",
    @SerialName("username")         val username        : String  = "",
    @SerialName("birth_date")       val birthDate       : String  = "",
    @SerialName("gender")           val gender          : String  = "",
    @SerialName("class_type")       val classType       : String  = "",
    @SerialName("height")           val height          : Int     = 0,
    @SerialName("weight")           val weight          : Double  = 0.0,
    @SerialName("experience_level") val experienceLevel : String  = "",
    @SerialName("goals")            val goals           : String  = "",
    @SerialName("is_premium")       var premium         : Boolean = false,
    @SerialName("target_weight")    val targetWeight    : Double? = null,
    @SerialName("target_calories")  val targetCalories  : Int?    = null,
    @SerialName("target_protein")   val targetProtein   : Double? = null,
    @SerialName("target_carbs")     val targetCarbs     : Double? = null,
    @SerialName("target_fat")       val targetFat       : Double? = null,
    @SerialName("created_at")       val createdAt       : Long    = 0L,
    @SerialName("updated_at")       val updatedAt       : Long    = 0L,
)