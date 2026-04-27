package org.fitverse.project.features.user.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    @SerialName("uid")              val uid: String,
    @SerialName("name")             val name: String,
    @SerialName("email")            val email: String,
    @SerialName("lastname")         val lastname: String,
    @SerialName("username")         val username: String,
    @SerialName("birth_date")       val birthDate: String,
    @SerialName("gender")           val gender: String,
    @SerialName("class_type")       val classType: String,
    @SerialName("height")           val height: Int,
    @SerialName("weight")           val weight: Double,
    @SerialName("experience_level") val experienceLevel: String,
    @SerialName("goals")            val goals: String,
    @SerialName("is_premium")       val isPremium: Boolean,
    @SerialName("target_weight")    val targetWeight: Double? = null,
    @SerialName("target_calories")  val targetCalories: Int? = null,
    @SerialName("target_protein")   val targetProtein: Double? = null,
    @SerialName("target_carbs")     val targetCarbs: Double? = null,
    @SerialName("target_fat")       val targetFat: Double? = null,
)


fun UserRequest.toDocument(): UserDocument = UserDocument(
    uid             = uid,
    name            = name,
    email           = email,
    lastname        = lastname,
    username        = username,
    birthDate       = birthDate,
    gender          = gender,
    classType       = classType,
    height          = height,
    weight          = weight,
    experienceLevel = experienceLevel,
    goals           = goals,
    isPremium       = isPremium,
    targetWeight    = targetWeight,
    targetCalories  = targetCalories,
    targetProtein   = targetProtein,
    targetCarbs     = targetCarbs,
    targetFat       = targetFat,
    createdAt       = System.currentTimeMillis(),
    updatedAt       = System.currentTimeMillis(),
)
