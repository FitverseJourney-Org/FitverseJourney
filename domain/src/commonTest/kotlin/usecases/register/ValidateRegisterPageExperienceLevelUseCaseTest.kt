package usecases.register

import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateRegisterPageExperienceLevelUseCaseTest {

    private val validateRegisterPageExperienceLevelUseCase = ValidateRegisterPageExperienceLevelUseCase()

    @Test
    fun validateEmptyField() {
        val result = validateRegisterPageExperienceLevelUseCase(
            registerExperienceLevel = RegisterExperienceLevel.NONE,
        )
        assertEquals(RegisterPageExperienceLevelValidationType.EmptyField, result)
    }

    @Test
    fun validateSuccess() {
        val result = validateRegisterPageExperienceLevelUseCase(
            registerExperienceLevel = RegisterExperienceLevel.BEGINNER,
        )
        assertEquals(RegisterPageExperienceLevelValidationType.Valid, result)
    }
}