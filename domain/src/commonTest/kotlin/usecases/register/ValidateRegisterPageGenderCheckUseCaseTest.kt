package usecases.register

import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateRegisterPageGenderCheckUseCaseTest {

    private val validateRegisterPageGenderUseCase = ValidateRegisterPageGenderUseCase()

    @Test
    fun validateEmptyField() {
        val result = validateRegisterPageGenderUseCase(
            registerGender = RegisterGender.NONE
        )
        assertEquals(RegisterPageGenderValidationType.EmptyField, result)
    }

    @Test
    fun validateSuccess() {
        val result = validateRegisterPageGenderUseCase(
            registerGender = RegisterGender.MALE
        )
        assertEquals(RegisterPageGenderValidationType.Valid, result)
    }

}