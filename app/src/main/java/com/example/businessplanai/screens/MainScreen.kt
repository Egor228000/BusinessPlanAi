package com.example.businessplanai.screens

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.businessplanai.R
import com.example.businessplanai.viewModel.MainViewModel


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Main(
    mainViewModel: MainViewModel,
    listState: LazyListState,
    onNavigateWatch: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onNavigateSetting: () -> Unit
) {


    val businessList = mainViewModel.businessList.collectAsState()//
    var deleteCard = remember { mutableStateOf<Int?>(null) }
    val listStateTwo = rememberLazyListState()
    val activity = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass(activity as Activity)

    val selectedCard =  remember { mutableStateOf<Int?>(null) }

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
    var sizeHeightCard = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 0.dp
        WindowWidthSizeClass.Medium -> 16.dp
        else -> 16.dp
    }

    var displayText = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 35
        WindowWidthSizeClass.Medium -> 80
        else -> 80
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
    var searcchBusinessCard by remember { mutableStateOf("") }


    val filteredList = businessList.value.filter {
        it.title.contains(searcchBusinessCard, ignoreCase = true)

    }

    var focus = LocalFocusManager.current
    deleteCard.value?.let { cardId ->
        Dialog(
            onDismissRequest = { deleteCard.value = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(widthFill)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onBackground)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            stringResource(R.string.dialogQuestion_1),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.background
                        )
                        Text(
                            stringResource(R.string.dialogText),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.background
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
                                .alpha(0.6f)
                                .padding(end = 16.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface)
                        ) {
                            Text(
                                stringResource(R.string.dialogYes),
                                color = MaterialTheme.colorScheme.background
                            )
                        }

                        Button(
                            onClick = {
                                mainViewModel.deleteBusiness(cardId)
                                onDeleteClick(cardId)
                                deleteCard.value = null
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier.widthIn(min = widthButton)
                        ) {
                            Text(
                                stringResource(R.string.dialogNo),
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.onPrimary,)


    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                MaterialTheme.colorScheme.onPrimary
            ),
            title = {
                Text(text = stringResource(R.string.titleMain),  color = MaterialTheme.colorScheme.background,)
            },
            actions =  {
                IconButton(
                    onClick = {
                        onNavigateSetting()
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_settings_24),
                        null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }

        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        OutlinedTextField(
            value = searcchBusinessCard,
            onValueChange = { searcchBusinessCard = it },
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 16.dp, end = 16.dp)

            ,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = Color(0x00FFFFFF),
                focusedBorderColor = Color(0x00FFFFFF),
            ),
            placeholder = {
                Text(
                    stringResource(R.string.titlePlaceholder),
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 23.sp

                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_search_24),
                    null, tint = MaterialTheme.colorScheme.surface
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.background,
                fontSize = 23.sp
            ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focus.clearFocus(force = true) }
            ),

            )
        LazyColumn(
            state = state,
            verticalArrangement = if (businessList.value.isEmpty()) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = if (businessList.value.isEmpty()) Alignment.CenterHorizontally else Alignment.CenterHorizontally,
            modifier = Modifier

                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            if (businessList.value.isEmpty()) {
                items(1) {
                    Text(
                        stringResource(R.string.EmptyNote),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                items(filteredList) { card ->
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    BusinessCard(
                        card.title,
                        card.description,
                        onNavigateWatch = { onNavigateWatch(card.id) }, // ➜ адаптивное поведение
                        onEditClick = { onEditClick(card.id) },
                        onDeleteClick = { deleteCard.value = card.id }, // <-- передано
                        fontSizeText,
                        displayText,
                        isSelected = selectedCard.value == card.id,
                        onSelect = { selectedCard.value = card.id },
                        sizeHeightCard
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessCard(
    title: String,
    description: String,
    onNavigateWatch: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    fontSizeText: TextUnit,
    displayText: Int,
    isSelected: Boolean,
    onSelect: () -> Unit,
    sizeHeightCard: Dp
) {
    var expanded = remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        else Color(0x00FFFFFF),
        animationSpec = tween(durationMillis = 400),
        label = "cardBackgroundColor"
    )
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)

    ) {
        Card(
            onClick ={
                onSelect()      // Выделяем карточку
                onNavigateWatch() // И навигация
            },
            modifier = Modifier

                .height(90.dp)

                .fillMaxWidth(1f),
            colors = CardDefaults.cardColors(backgroundColor)

            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = sizeHeightCard)

                ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onBackground)
                        .size(50.dp, 50.dp)


                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_docs_24),
                        null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
                Box {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            if (title.length >= displayText) title.take(displayText).drop(3) + "..." else title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.background,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Text(
                            text = if (description.length >= displayText) description.take(
                                displayText
                            ).drop(3) + "..." else description.drop(3),
                            maxLines = 1,
                            fontSize = fontSizeText,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box {
                IconButton(
                    onClick = {
                        expanded.value = true
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_more_vert_24),
                        null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    containerColor = MaterialTheme.colorScheme.onBackground
                ) {
                    DropdownMenuItem(
                        onClick = {
                            expanded.value = false
                            onEditClick()
                        },
                        text = {
                            Text(
                                stringResource(R.string.businessCardEdit),
                                color = MaterialTheme.colorScheme.background
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_edit_24),

                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background
                            )
                        },
                        colors = MenuDefaults.itemColors(MaterialTheme.colorScheme.onBackground)
                    )
                    DropdownMenuItem(
                        onClick = {
                            onDeleteClick()
                            expanded.value = false

                        },
                        text = {
                            Text(
                                stringResource(R.string.businessCardDelete),
                                color = MaterialTheme.colorScheme.background
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.outline_delete_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background
                            )
                        },
                        colors = MenuDefaults.itemColors(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}
