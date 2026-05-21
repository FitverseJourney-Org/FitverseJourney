package org.fitverse.presentation

import app.cash.turbine.test
import org.fitverse.domain.fakes.FakeAppAuthenticateRepository
import org.fitverse.domain.fakes.FakeAuthRepository
import org.fitverse.domain.fakes.FakeUserRepository
import org.fitverse.domain.models.errors.AuthError
import org.fitverse.domain.usecase.login.LoginUseCase
import org.fitverse.presentation.ui.authentication.login.LoginViewModel
import org.fitverse.presentation.ui.authentication.login.states.LoginAction
import org.fitverse.presentation.ui.authentication.login.states.LoginNavigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LoginViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var fakeAuth: FakeAuthRepository
    private lateinit var fakeUser: FakeUserRepository
    private lateinit var fakeAppAuth: FakeAppAuthenticateRepository
    private lateinit var viewModel: LoginViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuth    = FakeAuthRepository()
        fakeUser    = FakeUserRepository()
        fakeAppAuth = FakeAppAuthenticateRepository()
        viewModel   = LoginViewModel(
            loginUseCase = LoginUseCase(fakeAuth, fakeUser, fakeAppAuth)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── State mutations ───────────────────────────────────────────────────────

    @Test
    fun `EmailChanged atualiza state email`() {
        viewModel.onAction(LoginAction.EmailChanged("user@fit.com"))
        assertEquals("user@fit.com", viewModel.state.value.email)
    }

    @Test
    fun `PasswordChanged atualiza state password`() {
        viewModel.onAction(LoginAction.PasswordChanged("senha123"))
        assertEquals("senha123", viewModel.state.value.password)
    }

    @Test
    fun `TogglePasswordVisibility inverte isPasswordVisible`() {
        assertFalse(viewModel.state.value.isPasswordVisible)
        viewModel.onAction(LoginAction.TogglePasswordVisibility)
        assertTrue(viewModel.state.value.isPasswordVisible)
        viewModel.onAction(LoginAction.TogglePasswordVisibility)
        assertFalse(viewModel.state.value.isPasswordVisible)
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    @Test
    fun `NavigateToRegister emite ToRegister`() = runTest {
        viewModel.navigationState.test {
            viewModel.onAction(LoginAction.NavigateToRegister)
            assertEquals(LoginNavigation.ToRegister, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `NavigateToForgotPassword emite ToResetPassword`() = runTest {
        viewModel.navigationState.test {
            viewModel.onAction(LoginAction.NavigateToForgotPassword)
            assertEquals(LoginNavigation.ToResetPassword, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── Login validation ──────────────────────────────────────────────────────

    @Test
    fun `LoginClicked com campos vazios mostra snackbar de erro`() = runTest {
        viewModel.onAction(LoginAction.LoginClicked)
        assertNotNull(viewModel.state.value.snackBarData)
        assertEquals(0, fakeAuth.loginCallCount)
    }

    @Test
    fun `LoginClicked com email vazio mostra snackbar de erro`() = runTest {
        viewModel.onAction(LoginAction.PasswordChanged("senha123"))
        viewModel.onAction(LoginAction.LoginClicked)
        assertNotNull(viewModel.state.value.snackBarData)
        assertEquals(0, fakeAuth.loginCallCount)
    }

    // ── Login flow ────────────────────────────────────────────────────────────

    @Test
    fun `LoginClicked com credenciais validas emite ToHome`() = runTest {
        viewModel.onAction(LoginAction.EmailChanged("user@fit.com"))
        viewModel.onAction(LoginAction.PasswordChanged("senha123"))

        viewModel.navigationState.test {
            viewModel.onAction(LoginAction.LoginClicked)
            assertEquals(LoginNavigation.ToHome, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `LoginClicked com credenciais invalidas mostra snackbar de erro`() = runTest {
        fakeAuth.willFailWith(AuthError.InvalidCredentials("Credenciais inválidas"))
        viewModel.onAction(LoginAction.EmailChanged("wrong@fit.com"))
        viewModel.onAction(LoginAction.PasswordChanged("errada"))

        viewModel.onAction(LoginAction.LoginClicked)

        assertNotNull(viewModel.state.value.snackBarData)
        assertFalse(viewModel.state.value.isLoading)
    }
}
