package com.flipperdevices.bsb.appblocker.filter.composable.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_search_hint
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.material_ic_search
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBlockerSearchBarComposable(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        SearchBar(
            modifier = modifier,
            expanded = false,
            onExpandedChange = { },
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = { },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.appblocker_filter_search_hint),
                            fontSize = 16.sp,
                            fontFamily = LocalBusyBarFonts.current.pragmatica,
                            fontWeight = FontWeight.W400,
                            color = Color(color = 0xFFCAC4D0),
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.material_ic_search),
                            contentDescription = null,
                            tint = Color(color = 0xFFCAC4D0)
                        )
                    },
                )
            },
            content = {},
        )
    }
}

@Preview
@Composable
private fun AppBlockerSearchBarComposablePreview() {
    BusyBarThemeInternal {
        var query by remember { mutableStateOf("") }
        AppBlockerSearchBarComposable(
            query = query,
            onQueryChange = { query = it },
        )
    }
}
