package com.example.domain.usecase.register

import com.example.domain.model.authentication.register.RegisterAvatar
import com.example.domain.model.authentication.register.RegisterExperienceLevel
import com.example.domain.model.register.RegisterPageAvatarValidationType

class ValidateRegisterPageAvatarUseCase() {
    operator fun invoke(
        avatar: RegisterAvatar
    ) : RegisterPageAvatarValidationType {
        if (avatar == RegisterAvatar.NONE) {
            return RegisterPageAvatarValidationType.EmptyField
        }
        return RegisterPageAvatarValidationType.Valid
    }
}