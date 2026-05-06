package com.example.presentation.ui.authentication.register.components

import androidx.compose.runtime.Composable
import com.example.domain.models.validations.ValidationType
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
fun ValidationType.toMessage(): String = when (this) {
    ValidationType.BLANK          -> stringResource(Res.string.error_blank)
    ValidationType.TOO_SHORT      -> stringResource(Res.string.error_too_short)
    ValidationType.TOO_LONG       -> stringResource(Res.string.error_too_long)
    ValidationType.INVALID_FORMAT -> stringResource(Res.string.error_invalid_format)
    ValidationType.INVALID_AGE    -> stringResource(Res.string.error_invalid_age)
    ValidationType.OUT_OF_RANGE   -> stringResource(Res.string.error_out_of_range)
    ValidationType.INVALID_DATE   -> stringResource(Res.string.error_invalid_date)
    ValidationType.REQUIRED       -> stringResource(Res.string.error_required)
    ValidationType.SUCCESS        -> stringResource(Res.string.error_required)

}