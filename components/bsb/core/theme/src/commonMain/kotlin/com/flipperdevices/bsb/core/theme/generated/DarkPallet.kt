package com.flipperdevices.bsb.core.theme.generated

import androidx.compose.ui.graphics.Color
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Black
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Bluetooth
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Brand
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Danger
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Neutral
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Success
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Surface
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Transparent
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Transparent.BlackInvert
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Transparent.WhiteInvert
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Warning
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.White
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet.Wifi
import kotlin.Suppress

/**
 * Autogenerated code from https://github.com/LionZXY/FlipperPalletGenerator/
 */
@Suppress("MagicNumber", "LongMethod")
internal fun getDarkPallet(): BusyBarPallet = BusyBarPallet(
    black = Black(
        invert = Color(0xFFFFFFFF),
        onColor = Color(0xFF000000)
    ),
    white = White(
        invert = Color(0xFF000000),
        onColor = Color(0xFFFFFFFF)
    ),
    transparent = Transparent(
        blackInvert = BlackInvert(
            primary = Color(0x80FFFFFF),
            secondary = Color(0x4DFFFFFF),
            tertiary = Color(0x33FFFFFF),
            quaternary = Color(0x1AFFFFFF),
            quinary = Color(0x0DFFFFFF)
        ),
        whiteInvert = WhiteInvert(
            primary = Color(0x80000000),
            secondary = Color(0x4D000000),
            tertiary = Color(0x1A000000),
            quaternary = Color(0x0D000000),
            quinary = Color(0x05000000)
        )
    ),
    neutral = Neutral(
        primary = Color(0xFFE5E5E5),
        secondary = Color(0xFFCCCCCC),
        tertiary = Color(0xFF999999),
        quaternary = Color(0xFF666666),
        quinary = Color(0xFF333333),
        senary = Color(0xFF1A1A1A),
        septenary = Color(0xFF0D0D0D)
    ),
    surface = Surface(
        primary = Color(0xFF1A1A1A),
        secondary = Color(0xFF171717),
        tertiary = Color(0xFF000000)
    ),
    brand = Brand(
        primary = Color(0xFFFF5C16),
        secondary = Color(0x4DFF5C16),
        tertiary = Color(0x1AFF5C16)
    ),
    bluetooth = Bluetooth(
        primary = Color(0xFF2C72FA),
        secondary = Color(0x4D2C72FA),
        tertiary = Color(0x1A2C72FA)
    ),
    wifi = Wifi(
        primary = Color(0xFFA3E635),
        secondary = Color(0x4DA3E635),
        tertiary = Color(0x1AA3E635)
    ),
    success = Success(
        primary = Color(0xFF10B981),
        secondary = Color(0x4D10B981),
        tertiary = Color(0x1A10B981)
    ),
    danger = Danger(
        primary = Color(0xFFEF4444),
        secondary = Color(0x4DEF4444),
        tertiary = Color(0x1AEF4444)
    ),
    warning = Warning(
        primary = Color(0xFFF59E0B),
        secondary = Color(0x4DF59E0B),
        tertiary = Color(0x1AF59E0B)
    )
)
