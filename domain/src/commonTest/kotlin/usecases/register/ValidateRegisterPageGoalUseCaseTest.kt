package usecases.register

import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateRegisterPageGoalUseCaseTest {

    private val validateRegisterPageGoalUseCase = ValidateRegisterPageGoalsUseCase()

    @Test
    fun validateEmptyField() {
        val result = validateRegisterPageGoalUseCase(
            registerGoal = RegisterGoal.NONE,
        )
        assertEquals(RegisterPageGoalsValidationType.EmptyField, result)
    }

    @Test
    fun validateSuccess() {
        val result = validateRegisterPageGoalUseCase(
            registerGoal = RegisterGoal.LOSE_WEIGHT,
        )
        assertEquals(RegisterPageGoalsValidationType.Valid, result)
    }
}