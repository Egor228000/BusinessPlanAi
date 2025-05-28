package com.example.businessplanai.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.businessplanai.ui.theme.AppTheme
import com.example.businessplanai.viewModel.SettingViewModel
import com.example.businessplanai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    padding: PaddingValues,
    settingViewModel: SettingViewModel
) {

    val focus = LocalFocusManager.current

    val currentTheme by settingViewModel.appTheme.collectAsState()
    val serverIp by settingViewModel.serverIp.collectAsState()
    var ipAdress by remember { mutableStateOf(serverIp) }



    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        items(1) {
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Text(
                stringResource(R.string.settingDescription_1),
                color = MaterialTheme.colorScheme.background
            )
            Text(
                stringResource(R.string.settingDescription_2),
                color = MaterialTheme.colorScheme.background,
                fontSize = 15.sp
            )

        }
        items(1) {
            OutlinedTextField(
                value = ipAdress,
                onValueChange = { ipAdress = it },
                modifier = Modifier
                    .fillMaxWidth(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = Color(0x00FFFFFF),
                    focusedBorderColor = Color(0x00FFFFFF),
                ),
                placeholder = {
                    Text(
                        "192.000.0.000:1234",
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 20.sp

                    )
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 20.sp
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focus.clearFocus(force = true) }
                ),
                prefix = {
                    Text(
                        "http://",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 20.sp
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            settingViewModel.setServerIp(ipAdress)
                        },
                        enabled = ipAdress.isNotEmpty()
                    ) {
                        Icon(
                            Icons.Default.Done,
                            null,
                            tint = MaterialTheme.colorScheme.background,
                        )
                    }
                }
            )

        }
        items(1) {

            Text(
                stringResource(R.string.settingThemeTitle),
                color = MaterialTheme.colorScheme.background
            )

            val options = listOf(
                AppTheme.SYSTEM to stringResource(R.string.settingTheme_1),
                AppTheme.LIGHT to stringResource(R.string.settingTheme_2),
                AppTheme.DARK to stringResource(R.string.settingTheme_3),
                AppTheme.LIGHTRed to  stringResource(R.string.settingTheme_4),
                AppTheme.DARKRed to stringResource(R.string.settingTheme_5),
                AppTheme.LIGHTGreen to  stringResource(R.string.settingTheme_6),
                AppTheme.DARKGreen to stringResource(R.string.settingTheme_7),
            )


            options.forEach { (theme, label) ->
                Row {
                    AssistChip(
                        onClick = { settingViewModel.setTheme(theme) },
                        label = { Text(label, color = MaterialTheme.colorScheme.background) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (currentTheme == theme) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            labelColor = if (currentTheme == theme) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ),
                        modifier = Modifier.width(150.dp)
                    )
                }
            }

        }
    }
}