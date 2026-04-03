package usecases.register

import com.example.domain.model.authentication.register.RegisterGender
import com.example.domain.model.register.RegisterPageGenderValidationType
import com.example.domain.usecase.register.ValidateRegisterPageGenderUseCase
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