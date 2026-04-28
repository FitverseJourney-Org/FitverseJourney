package usecases.register

import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateRegisterPageCredentialsInputsUseCaseTest {

    private val validateRegisterPageCredentialsInputsUseCase = ValidateRegisterPageCredentialsUseCase()

    @Test
    fun validateEmailEmpty() {
        val result = validateRegisterPageCredentialsInputsUseCase(
            email = "",
            password = "12345678"
        )
        assertEquals(RegisterPageCredentialsValidationType.EmptyEmail, result)
    }

    @Test
    fun validatePasswordEmpty() {
        val result = validateRegisterPageCredentialsInputsUseCase(
            email = "John@gmail.com",
            password = ""
        )
        assertEquals(RegisterPageCredentialsValidationType.EmptyPassword, result)
    }

    @Test
    fun validateEmptyFields() {
        val result = validateRegisterPageCredentialsInputsUseCase(
            email = "",
            password = ""
        )
        assertEquals(RegisterPageCredentialsValidationType.EmptyFields, result)
    }

    @Test
    fun validateInvalidEmail() {
        val result = validateRegisterPageCredentialsInputsUseCase(
            email = "john.doe",
            password = "12345678"
        )
        assertEquals(RegisterPageCredentialsValidationType.InvalidEmail, result)
    }

    @Test
    fun validateWeakPassword() {
        val result = validateRegisterPageCredentialsInputsUseCase(
            email = "john.doe@gmail.com",
            password = "1234"
        )
        assertEquals(RegisterPageCredentialsValidationType.WeakPassword, result)
    }

    @Test
    fun validateSuccessInputs() {
        val result = validateRegisterPageCredentialsInputsUseCase(
            email = "john.doe@gmail.com",
            password = "12345678"
        )
        assertEquals(RegisterPageCredentialsValidationType.Valid, result)
    }

}