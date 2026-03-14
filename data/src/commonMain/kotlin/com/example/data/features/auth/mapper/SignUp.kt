package com.example.data.features.auth.mapper

import com.example.data.features.auth.model.signUp.FitnessGoalRequest
import com.example.data.features.auth.model.signUp.FitnessLevelRequest
import com.example.data.features.auth.model.signUp.GenderRequest
import com.example.data.features.auth.model.signUp.SignUpRequest
import com.example.domain.model.authentication.register.Gender
import com.example.domain.model.authentication.register.FitnessLevel
import com.example.domain.model.authentication.register.FitnessGoal
import com.example.domain.model.authentication.register.SignUp

//fun SignUpRequest.toDomain(): SignUp {
//    return SignUp(
//        name = name,
//        email = email,
//        password = password,
//        gender = genderRequest.toDomain(),
//        birthDate = birthDate,
//        heightCm = heightCm,
//        weightKg = weightKg,
//        fitnessLevel = fitnessLevelRequest.toDomain(),
//        fitnessGoal = fitnessGoal.toDomain()
//    )
//}
//
//fun GenderRequest.toDomain(): Gender {
//    return when(this) {
//        GenderRequest.MALE -> Gender.MALE
//        GenderRequest.FEMALE -> Gender.FEMALE
//        GenderRequest.OTHER -> Gender.OTHER
//    }
//}
//fun FitnessLevelRequest.toDomain(): FitnessLevel {
//    return when(this){
//        FitnessLevelRequest.BEGINNER -> FitnessLevel.BEGINNER
//        FitnessLevelRequest.INTERMEDIATE -> FitnessLevel.INTERMEDIATE
//        FitnessLevelRequest.ADVANCED -> FitnessLevel.ADVANCED
//    }
//}
//
//fun FitnessGoalRequest.toDomain(): FitnessGoal {
//    return when(this){
//        FitnessGoalRequest.LOSE_WEIGHT -> FitnessGoal.LOSE_WEIGHT
//        FitnessGoalRequest.GAIN_MUSCLE -> FitnessGoal.GAIN_MUSCLE
//        FitnessGoalRequest.MAINTAIN -> FitnessGoal.MAINTAIN
//        FitnessGoalRequest.IMPROVE_ENDURANCE -> FitnessGoal.IMPROVE_ENDURANCE
//    }
//}