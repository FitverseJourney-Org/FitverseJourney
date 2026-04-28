package usecases.register

import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateRegisterPageIntroductionInputsUseCaseTest {

    private val validateRegisterPageIntroductionInputsUseCase = ValidateRegisterPageIntroductionUseCase()

    @Test
    fun validateFirstName() {
        val result = validateRegisterPageIntroductionInputsUseCase(
            firstName = "",
            lastName = "Doe"
        )
        assertEquals(RegisterPageIntroductionValidationType.NoFirstName, result)
    }

    @Test
    fun validateLastName() {
        val result = validateRegisterPageIntroductionInputsUseCase(
            firstName = "John",
            lastName = ""
        )
        assertEquals(RegisterPageIntroductionValidationType.NoLastName, result)
    }

    @Test
    fun validateEmptyFields() {
        val result = validateRegisterPageIntroductionInputsUseCase(
            firstName = "",
            lastName = ""
        )
        assertEquals(RegisterPageIntroductionValidationType.EmptyFields, result)
    }

    @Test
    fun validateMinLengthFirstName() {
        val result = validateRegisterPageIntroductionInputsUseCase(
            firstName = "Jo",
            lastName = "Doe"
        )
        assertEquals(RegisterPageIntroductionValidationType.MinLengthFirstName, result)
    }

    @Test
    fun validateMinLengthLastName() {
        val result = validateRegisterPageIntroductionInputsUseCase(
            firstName = "John",
            lastName = "Do"
        )
        assertEquals(RegisterPageIntroductionValidationType.MinLengthLastName, result)
    }

    @Test
    fun validateSuccessInputs() {
        val result = validateRegisterPageIntroductionInputsUseCase(
            firstName = "John",
            lastName = "Doe"
        )
        assertEquals(RegisterPageIntroductionValidationType.Valid, result)
    }
}