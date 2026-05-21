package org.fitverse.presentation.ui.authentication.register.states

sealed interface RegisterNavigation {
    data object ToLogin : RegisterNavigation
}
