package usecases.register

import com.example.domain.model.authentication.register.RegisterAvatar
import com.example.domain.model.register.RegisterPageAvatarValidationType
import com.example.domain.usecase.register.ValidateRegisterPageAvatarUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateRegisterPageAvatarUseCaseTest {

    private val validateRegisterPageGoalUseCase = ValidateRegisterPageAvatarUseCase()

    @Test
    fun validateEmptyField() {
        val result = validateRegisterPageGoalUseCase(
            avatar = RegisterAvatar.NONE,
        )
        assertEquals(RegisterPageAvatarValidationType.EmptyField, result)
    }

    @Test
    fun validateSuccess() {
        val result = validateRegisterPageGoalUseCase(
            avatar = RegisterAvatar.ASSASSIN,
        )
        assertEquals(RegisterPageAvatarValidationType.Valid, result)
    }
}