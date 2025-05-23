package com.example.businessplanai.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.businessplanai.AppDatabase
import com.example.businessplanai.R
import com.example.businessplanai.routes.ScreenRoute
import com.example.businessplanai.ui.theme.BackgroundDark
import com.example.businessplanai.viewModel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Main(
    padding: PaddingValues,
    mainViewModel: MainViewModel,
    navigation: NavHostController,
    onNoteSelected: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit, // ✅ исправлено
    onDeleteSelectedNote: (Int) -> Unit = {},
    listState: LazyListState
) {
    val businessList = mainViewModel.businessList.collectAsState()//
    var deleteCard = remember { mutableStateOf<Int?>(null) }
    val listStateTwo = rememberLazyListState()
    val activity = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass(activity as Activity)

    // Состояния для LazyColumn
    var state = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> listState
        WindowWidthSizeClass.Medium -> listStateTwo
        else -> listStateTwo
    }

    // Размер карточек и текста
    var fontSizeText = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 16.sp
        WindowWidthSizeClass.Medium -> 15.sp
        else -> 16.sp
    }
    var sizeHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 120.dp
        WindowWidthSizeClass.Medium -> 85.dp
        else -> 115.dp
    }

    var displayText = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 70
        WindowWidthSizeClass.Medium -> 20
        else -> 45
    }

    // Dialog size
    var widthFill = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 1f
        WindowWidthSizeClass.Medium -> 0.6f
        else -> 0.45f
    }

    var widthButton = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 260.dp
        WindowWidthSizeClass.Medium -> 280.dp
        else -> 400.dp
    }

    Column {
        TopAppBar(
            title = { Text("Главная") },
        )
        LazyColumn(
            state = state,
            verticalArrangement = if (businessList.value.isEmpty()) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = if (businessList.value.isEmpty()) Alignment.CenterHorizontally else Alignment.CenterHorizontally,
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            if (businessList.value.isEmpty()) {
                items(1) {
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(
                            if (MaterialTheme.colorScheme.background == BackgroundDark) {
                                R.raw.empty_light
                            } else {
                                R.raw.empty_dark
                            }
                        )
                    )
                    LottieAnimation(
                        composition,
                        modifier = Modifier.fillMaxWidth(1f),
                    )
                }
            } else {
                items(businessList.value) { card ->
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    BusinessCard(
                        navigation,
                        card.id,
                        card.title,
                        card.description,
                        onClick = { onNoteSelected(card.id) }, // ➜ адаптивное поведение
                        onEditClick = { navigation.navigate("${ScreenRoute.Edit.route}/${card.id}") },
                        onDeleteClick = { deleteCard.value = card.id },
                        fontSizeText,
                        sizeHeight,
                        displayText
                    )
                }
            }
        }

        deleteCard.value?.let { cardIdToDelete ->
            Dialog(
                onDismissRequest = { deleteCard.value = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(widthFill)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Вы действительно хотите удалить?",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Это действие необратимо",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { deleteCard.value = null },
                                modifier = Modifier
                                    .alpha(0.4f)
                                    .padding(end = 16.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Text("Нет, отмена", color = MaterialTheme.colorScheme.surface)
                            }

                            Button(
                                onClick = {
                                    mainViewModel.deleteBusiness(cardIdToDelete)
                                    onDeleteSelectedNote(cardIdToDelete)
                                    deleteCard.value = null
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.widthIn(min = widthButton)
                            ) {
                                Text("Да, удалить", color = MaterialTheme.colorScheme.surface)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessCard(
    navigation: NavHostController,
    id: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    fontSizeText: TextUnit,
    sizeHeight: Dp,
    displayText: Int
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Card(
            modifier = Modifier
                .height(sizeHeight)
                .fillMaxWidth(1f),
            onClick = onClick,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        ) {
            Box {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.padding(top = 6.dp))
                    Text(
                        text = if (description.length >= displayText) description.take(displayText)
                            .drop(3) + "..." else description.drop(3),
                        maxLines = 2,
                        fontSize = fontSizeText,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                IconButton(
                    onClick = {
                        expanded = true
                    },
                ) {
                    Icon(
                        Icons.Default.MoreVert, null,
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onEditClick()
                        },
                        text = { Text("Редактировать") },
                        leadingIcon = {
                            Icon(Icons.Default.Edit, contentDescription = null)
                        },
                        colors = MenuDefaults.itemColors(MaterialTheme.colorScheme.onBackground)
                    )
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onDeleteClick()
                        },
                        text = { Text("Удалить") },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        },
                        colors = MenuDefaults.itemColors(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}
