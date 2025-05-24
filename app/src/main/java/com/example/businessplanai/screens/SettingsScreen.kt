package com.example.businessplanai.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.businessplanai.ui.theme.AppTheme
import com.example.businessplanai.viewModel.SettingViewModel

@Composable
fun Settings(
    navigation: NavHostController,
    padding: PaddingValues,
    settingViewModel: SettingViewModel
) {

    var ipAdress by remember { mutableStateOf("") }
    val focus = LocalFocusManager.current

    val currentTheme by settingViewModel.appTheme.collectAsState()
    val serverIp by settingViewModel.serverIp.collectAsState()



    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        items(1) {
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Text("Адрес вашего сервера LM-studio", color = MaterialTheme.colorScheme.background)
            Text(
                "Укажите адресс сервера и не забдьте указать порт(последние 4 цифры).",
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
                }
            )

        }
        items(1) {

            Text("Темы приложения", color = MaterialTheme.colorScheme.background)

            val options = listOf(
                AppTheme.SYSTEM to "Системная",
                AppTheme.LIGHT to "Светлая",
                AppTheme.DARK to "Тёмная",
                AppTheme.LIGHTRed to "Светлая красная",
                AppTheme.DARKRed to "Тёмная красная",
                AppTheme.LIGHTGreen to "Светлая зелёная",
                AppTheme.DARKGreen to "Тёмная зелёная"
            )

            options.forEach { (theme, label) ->
                Row {
                    AssistChip(
                        onClick = { settingViewModel.setTheme(theme) },
                        label = { Text(label) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (currentTheme == theme) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            labelColor = if (currentTheme == theme) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
}