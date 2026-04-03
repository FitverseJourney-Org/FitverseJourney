package usecases.register

import com.example.domain.model.authentication.register.RegisterMacros
import com.example.domain.model.register.RegisterPageMacrosValidationType
import com.example.domain.usecase.register.ValidateRegisterPageMacrosUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateRegisterPageMacrosUseCaseTest {

    private val validateRegisterPageGoalUseCase = ValidateRegisterPageMacrosUseCase()

    @Test
    fun validateEmptyFields() {
        val result = validateRegisterPageGoalUseCase(
            macros = RegisterMacros(
                calories = 0,
                carbohydrates = 0,
                proteins = 0,
                fats = 0,
                waterMl = 0
            )
        )
        assertEquals(RegisterPageMacrosValidationType.EmptyFields, result)
    }

    @Test
    fun validateNoCalories() {
        val result = validateRegisterPageGoalUseCase(
            macros = RegisterMacros(
                calories = 0,
                carbohydrates = 2000,
                proteins = 100,
                fats = 100,
                waterMl = 2000
            )
        )
        assertEquals(RegisterPageMacrosValidationType.NoCalories, result)
    }

    @Test
    fun validateNoCarbohydrates() {
        val result = validateRegisterPageGoalUseCase(
            macros = RegisterMacros(
                calories = 2000,
                carbohydrates = 0,
                proteins = 100,
                fats = 100,
                waterMl = 2000
            )
        )
        assertEquals(RegisterPageMacrosValidationType.NoCarbohydrates, result)
    }

    @Test
    fun validateNoProtein() {
        val result = validateRegisterPageGoalUseCase(
            macros = RegisterMacros(
                calories = 2000,
                carbohydrates = 2000,
                proteins = 0,
                fats = 100,
                waterMl = 2000
            )
        )
        assertEquals(RegisterPageMacrosValidationType.NoProteins, result)
    }

    @Test
    fun validateNoFat() {
        val result = validateRegisterPageGoalUseCase(
            macros = RegisterMacros(
                calories = 2000,
                carbohydrates = 2000,
                proteins = 100,
                fats = 0,
                waterMl = 2000
            )
        )
        assertEquals(RegisterPageMacrosValidationType.NoFats, result)
    }

    @Test
    fun validateNoWater() {
        val result = validateRegisterPageGoalUseCase(
            macros = RegisterMacros(
                calories = 2000,
                carbohydrates = 2000,
                proteins = 100,
                fats = 100,
                waterMl = 0
            )
        )
        assertEquals(RegisterPageMacrosValidationType.NoWater, result)
    }
}