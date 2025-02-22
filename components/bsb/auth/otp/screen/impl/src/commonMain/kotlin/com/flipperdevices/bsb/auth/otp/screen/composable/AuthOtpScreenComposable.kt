package com.flipperdevices.bsb.auth.otp.screen.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.auth.otp.screen.impl.generated.resources.Res
import busystatusbar.components.bsb.auth.otp.screen.impl.generated.resources.login_otp_screen_code_resend
import com.flipperdevices.bsb.auth.common.composable.BusyBarButtonComposable
import com.flipperdevices.bsb.auth.common.composable.appbar.LogInAppBarComposable
import com.flipperdevices.bsb.auth.common.composable.subaction.AuthTextSubActionComposable
import com.flipperdevices.bsb.auth.otp.screen.model.AuthOtpScreenState
import com.flipperdevices.bsb.auth.otp.screen.model.InternalAuthOtpType
import com.flipperdevices.bsb.core.markdown.ComposableMarkdown
import com.flipperdevices.bsb.core.markdown.markdownColor
import com.flipperdevices.bsb.core.markdown.markdownTypography
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("LongMethod")
fun AuthOtpScreenComposable(
    otpType: InternalAuthOtpType,
    onBack: () -> Unit,
    otpCodeFieldComposable: @Composable (Modifier) -> Unit,
    onConfirm: () -> Unit,
    onResend: () -> Unit,
    authOtpScreenState: AuthOtpScreenState,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogInAppBarComposable(
            text = otpType.textTitle,
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .imePadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthOtpScreenHeaderComposable(modifier = Modifier, otpType = otpType)

            when (authOtpScreenState) {
                AuthOtpScreenState.CheckCodeInProgress,
                is AuthOtpScreenState.RequestEmailInProgress,
                is AuthOtpScreenState.WaitingForInput -> {
                    otpCodeFieldComposable(Modifier.padding(top = 16.dp))

                    BusyBarButtonComposable(
                        modifier = Modifier
                            .padding(vertical = 32.dp)
                            .fillMaxWidth()
                            .bringIntoViewRequester(bringIntoViewRequester),
                        text = otpType.textCodeBtn,
                        onClick = onConfirm,
                        inProgress = when (authOtpScreenState) {
                            is AuthOtpScreenState.RequestEmailInProgress,
                            AuthOtpScreenState.ExpiryVerificationCode,
                            is AuthOtpScreenState.WaitingForInput -> false

                            AuthOtpScreenState.CheckCodeInProgress -> true
                        },
                        disabled = authOtpScreenState.inProgress
                    )
                }

                AuthOtpScreenState.ExpiryVerificationCode -> {
                    CodeExpireComposable(
                        modifier = Modifier.padding(vertical = 32.dp),
                        otpType = otpType,
                        onResend = onResend
                    )
                }
            }

            ComposableMarkdown(
                content = stringResource(otpType.textCodeDescMarkdown),
                typography = markdownTypography(
                    TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
                    )
                ),
                colors = markdownColor(LocalPallet.current.neutral.secondary)
            )

            AuthTextSubActionComposable(
                modifier = Modifier
                    .padding(top = 32.dp),
                onClick = onResend,
                text = Res.string.login_otp_screen_code_resend,
                inProgress = authOtpScreenState.inProgress,
                disabled = when (authOtpScreenState) {
                    AuthOtpScreenState.CheckCodeInProgress,
                    is AuthOtpScreenState.WaitingForInput,
                    AuthOtpScreenState.ExpiryVerificationCode -> false

                    is AuthOtpScreenState.RequestEmailInProgress -> {
                        authOtpScreenState.launchedManually
                    }
                }
            )
        }
    }
}
