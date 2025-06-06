package com.example.businessplanai.screens

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businessplanai.R
import com.example.businessplanai.ui.theme.AppTheme
import com.example.businessplanai.viewModel.SettingViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    settingViewModel: SettingViewModel,
    onBack: () -> Unit,
) {
    val currentTheme by settingViewModel.appTheme.collectAsState()
    val context = LocalContext.current
    val resources = context.resources

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                MaterialTheme.colorScheme.onPrimary
            ),
            title = {
                Text(
                    stringResource(R.string.titleSetting),
                    color = MaterialTheme.colorScheme.background
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBack()
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            items(1) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    stringResource(R.string.settingDescription_1),
                    color = MaterialTheme.colorScheme.background
                )
            }
            items(1) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(Modifier.height(16.dp))

                    SimpleModelLoader(
                        onModelSelected = { path ->
                            settingViewModel.saveModelPath(path)
                        },
                        resources
                    )
                    Spacer(Modifier.height(16.dp))


                }
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
                    AppTheme.LIGHTRed to stringResource(R.string.settingTheme_4),
                    AppTheme.DARKRed to stringResource(R.string.settingTheme_5),
                    AppTheme.LIGHTGreen to stringResource(R.string.settingTheme_6),
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
}

@Composable
fun SimpleModelLoader(
    onModelSelected: (String) -> Unit,
    resources: Resources
) {
    val context = LocalContext.current
    var status by remember { mutableStateOf("") }

    // Ланчер для выбора любого файла (GetContent → "*/*")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            status = resources.getString(R.string.settingNotFile)
            return@rememberLauncherForActivityResult
        }

        // Получаем имя файла через ContentResolver
        val fileName = context.getFileName(uri) ?: ""
        if (!fileName.endsWith(".task")) {
            status =  resources.getString(R.string.settingSelectedFile)
            return@rememberLauncherForActivityResult
        }

        // Копируем файл из полученного URI во внутреннее хранилище приложения (context.filesDir)
        val destinationFile = File(context.filesDir, fileName)
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            // Отдаём полный путь во ViewModel (которая сохранит его в DataStore)
            onModelSelected(destinationFile.absolutePath)
            status = resources.getString(R.string.settingYesModel)
        } catch (e: Exception) {
            status = resources.getString(R.string.settingNoOpenFile)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { launcher.launch("*/*") },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface)
        ) {
            Text(
                text = resources.getString(R.string.settingSelectedModel),
                color = MaterialTheme.colorScheme.background
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = status,
            color = MaterialTheme.colorScheme.background,
            fontSize = 14.sp
        )
    }
}

// Вспомогательный метод для извлечения имени файла из Uri
fun Context.getFileName(uri: Uri): String? {
    var name: String? = null
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) {
                name = cursor.getString(index)
            }
        }
    }
    return name
}