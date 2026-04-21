package com.example.remote.util

object ApiConstants {
    // Base URLs
    const val BASE_URL = "http://localhost:8080"
    const val BASE_URL_DEV = "https://dev-api.fitverseapp.com/v1"

    // Endpoints
    object Endpoints {
        // User
        const val USERS = "/users"
        const val USER_BY_ID = "/users/{id}"
        const val USER_BY_EMAIL = "/users/email/{email}"

        // Workout
        const val WORKOUTS = "/workouts"
        const val WORKOUT_BY_ID = "/workouts/{id}"

        // Meal
        const val MEALS = "/meals"
        const val MEAL_BY_ID = "/meals/{id}"

        // Auth
        const val LOGIN = "/auth/login"
        const val REGISTER = "/auth/register"
        const val REFRESH_TOKEN = "/auth/refresh"
    }

    // Headers
    object Headers {
        const val AUTHORIZATION = "Authorization"
        const val CONTENT_TYPE = "Content-Type"
        const val ACCEPT = "Accept"
    }

    // Timeout
    const val CONNECT_TIMEOUT = 15_000L
    const val READ_TIMEOUT = 30_000L
    const val WRITE_TIMEOUT = 30_000L
}