package com.example.businessplanai.screens

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.businessplanai.R
import com.example.businessplanai.routes.ScreenRoute
import com.example.businessplanai.viewModel.MainViewModel
import com.example.businessplanai.viewModel.WatchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotesAdaptiveScreen(
    padding: PaddingValues,
    mainViewModel: MainViewModel,
    watchViewModel: WatchViewModel,
    navigation: NavHostController,
    listState: LazyListState
) {
    val activity = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass(activity as Activity)
    val isExpandedScreen = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium
    var selectedNoteId by remember { mutableStateOf<Int?>(null) }
    var stateFloat = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 0.20f
        WindowWidthSizeClass.Medium -> 0.30f
        else -> 0.20f
    }

    if (isExpandedScreen) {
        var expanded by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        Row(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .weight(stateFloat)
            ) {
                Main(
                    padding,
                    mainViewModel,
                    navigation,
                    onNoteSelected = { id -> selectedNoteId = id },
                    onDeleteClick = { deletedId ->
                        if (selectedNoteId == deletedId) {
                            selectedNoteId = null
                        }
                        mainViewModel.deleteBusiness(deletedId)
                    },
                    listState = listState
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.5f)
            ) {

                if (selectedNoteId != null) {
                    Column {

                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.onPrimary),

                            title = {
                                Text(
                                    "Просмотр",
                                    color = MaterialTheme.colorScheme.background,

                                    )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = { selectedNoteId = null }
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack, null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        expanded = true
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_arrow_downward_24),
                                        null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    containerColor = MaterialTheme.colorScheme.onBackground
                                ) {
                                    DropdownMenuItem(
                                        onClick = {
                                            scope.launch(Dispatchers.IO) {
                                                expanded = false

                                                watchViewModel.saveTextToDownloads(
                                                    context,
                                                    "Бизнес_план.pdf",
                                                    "application/pdf",
                                                    watchViewModel.getCurrentBusinessText()
                                                )
                                            }
                                        },
                                        text = { Text("PDF") },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = null
                                            )
                                        },
                                        colors = MenuDefaults.itemColors(
                                            MaterialTheme.colorScheme.background
                                        )
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            scope.launch(Dispatchers.IO) {
                                                expanded = false

                                                watchViewModel.saveTextToDownloads(
                                                    context = context,
                                                    fileName = "бизнес_план.docx",
                                                    mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                                    content = watchViewModel.getCurrentBusinessText()
                                                )
                                            }
                                        },
                                        text = { Text("Word") },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.background
                                            )
                                        },
                                        colors = MenuDefaults.itemColors(
                                            MaterialTheme.colorScheme.background
                                        )
                                    )
                                }
                            }
                        )

                        Watch(
                            padding,
                            watchViewModel,
                            navigation,
                            id = selectedNoteId,
                            listState
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Выберите заметку", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    } else {
        // Маленький экран



            Main(
                padding,
                mainViewModel,
                navigation,
                onNoteSelected = { id ->
                    navigation.navigate("${ScreenRoute.Watch.route}/$id")
                },
                onDeleteClick = { deletedId ->
                    mainViewModel.deleteBusiness(deletedId)
                },
                onDeleteSelectedNote = { deletedId ->
                    if (selectedNoteId == deletedId) {
                        selectedNoteId = null
                    }
                },
                listState
            )



    }
}