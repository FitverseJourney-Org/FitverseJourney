package com.example.domain.models.friends

/**
 * Sealed class para ordenação type-safe
 */
enum class SortOrder {
    ASCENDING,
    DESCENDING;

    fun toggle(): SortOrder = when (this) {
        ASCENDING -> DESCENDING
        DESCENDING -> ASCENDING
    }

    val label: String
        get() = when (this) {
            ASCENDING -> "A-Z"
            DESCENDING -> "Z-A"
        }
}