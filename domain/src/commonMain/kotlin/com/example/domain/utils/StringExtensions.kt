package com.example.domain.utils

private val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && EMAIL_REGEX.matches(this)
}
fun String.isValidPassword(): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$".toRegex()
    return this.matches(passwordRegex)
}
fun String.isEmptyInput(): Boolean {
    return this.trim().isEmpty()
}