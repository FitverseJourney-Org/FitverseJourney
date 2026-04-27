package com.example.presentation.screens.ui.authentication.register.components

import androidx.compose.runtime.Composable
import com.example.domain.models.validations.ValidationError
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.error_blank
import fitversejourneyapp.presentation.generated.resources.error_invalid_age
import fitversejourneyapp.presentation.generated.resources.error_invalid_date
import fitversejourneyapp.presentation.generated.resources.error_invalid_format
import fitversejourneyapp.presentation.generated.resources.error_out_of_range
import fitversejourneyapp.presentation.generated.resources.error_required
import fitversejourneyapp.presentation.generated.resources.error_too_long
import fitversejourneyapp.presentation.generated.resources.error_too_short
import org.jetbrains.compose.resources.stringResource

@Composable
fun ValidationError.toMessage(): String = when (this) {
    ValidationError.BLANK          -> stringResource(Res.string.error_blank)
    ValidationError.TOO_SHORT      -> stringResource(Res.string.error_too_short)
    ValidationError.TOO_LONG       -> stringResource(Res.string.error_too_long)
    ValidationError.INVALID_FORMAT -> stringResource(Res.string.error_invalid_format)
    ValidationError.INVALID_AGE    -> stringResource(Res.string.error_invalid_age)
    ValidationError.OUT_OF_RANGE   -> stringResource(Res.string.error_out_of_range)
    ValidationError.INVALID_DATE   -> stringResource(Res.string.error_invalid_date)
    ValidationError.REQUIRED       -> stringResource(Res.string.error_required)
}