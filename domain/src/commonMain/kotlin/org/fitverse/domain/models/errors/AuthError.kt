package org.fitverse.domain.models.errors

sealed class AuthError(message: String) : Exception(message) {
    class InvalidCredentials(msg: String)   : AuthError(msg)
    class UserNotFound(msg: String)         : AuthError(msg)
    class EmailAlreadyInUse(msg: String)    : AuthError(msg)
    class WeakPassword(msg: String)         : AuthError(msg)
    class NetworkError(msg: String)         : AuthError(msg)
    class TooManyRequests(msg: String)      : AuthError(msg)
    class ReauthRequired(msg: String)       : AuthError(msg)
    class ServiceUnavailable(msg: String)   : AuthError(msg)
    class Unknown(msg: String)              : AuthError(msg)
}