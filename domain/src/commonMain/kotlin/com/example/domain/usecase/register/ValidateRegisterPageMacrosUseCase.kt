package com.example.domain.usecase.register

import com.example.domain.model.authentication.register.RegisterMacros
import com.example.domain.model.register.RegisterPageMacrosValidationType

class ValidateRegisterPageMacrosUseCase {

    operator fun invoke(macros: RegisterMacros): RegisterPageMacrosValidationType {
        // 1. Validação se TODOS forem 0 (Nenhum campo preenchido)

        // 2. Validações individuais (se algum específico for 0)
        if (macros.calories == 0) {
            return RegisterPageMacrosValidationType.NoCalories
        }
        if (macros.carbohydrates == 0) {
            return RegisterPageMacrosValidationType.NoCarbohydrates
        }
        if (macros.proteins == 0) {
            return RegisterPageMacrosValidationType.NoProteins
        }
        if (macros.fats == 0) {
            return RegisterPageMacrosValidationType.NoFats
        }
        if (macros.waterMl == 0) {
            return RegisterPageMacrosValidationType.NoWater
        }

        // 3. Se passou por tudo, está válido!
        return RegisterPageMacrosValidationType.Valid
    }
}