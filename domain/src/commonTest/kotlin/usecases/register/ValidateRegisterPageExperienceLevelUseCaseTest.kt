package usecases.register

import com.example.domain.model.authentication.register.RegisterExperienceLevel
import com.example.domain.model.register.RegisterPageExperienceLevelValidationType
import com.example.domain.usecase.register.ValidateRegisterPageExperienceLevelUseCase
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