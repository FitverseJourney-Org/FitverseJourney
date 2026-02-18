package com.example.domain.model.dashboard


data class TaskItem(
    val id: String,
    val title: String,
    val description: String = "",
    val xp: Int = 10,
    var completed: Boolean = false,
    val iconType: TaskIcon = TaskIcon.GENERIC
)
