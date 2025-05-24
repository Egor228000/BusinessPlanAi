package com.example.businessplanai.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.businessplanai.AppDatabase
import com.example.businessplanai.R
import com.example.businessplanai.ui.theme.BackgroundDark
import com.example.businessplanai.viewModel.AddViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.bouncycastle.crypto.params.Blake3Parameters.context

@Composable
fun AddPlan(
    padding: PaddingValues,
    addViewModel: AddViewModel,
    navigation: NavHostController,
) {
    var nameBusiness by remember { mutableStateOf("") }
    var pointBusiness by remember { mutableStateOf("") }
    var auditoriumBusiness by remember { mutableStateOf("") }
    var advantagesBusiness by remember { mutableStateOf("") }
    var monetizationBusiness by remember { mutableStateOf("") }
    var barriersAndSolutionsBusiness by remember { mutableStateOf("") }
    val isLoading = addViewModel.isLoading.collectAsState()
    val isLoadingNavigate = addViewModel.isLoadingNavigate.collectAsState()
    val focus = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isConnected by addViewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            delay(2000)
            if (!addViewModel.isConnected.value) {
                Toast.makeText(
                    context,
                    "Нет интернета",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Column(
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            items(1) {
                OutlinedTextField(
                    value = nameBusiness,
                    onValueChange = { nameBusiness = it },
                    modifier = Modifier.fillMaxWidth(1f),
                    maxLines = 1,
                    placeholder = {
                        Text("Мир кофе", style = MaterialTheme.typography.bodySmall)
                    },
                    label = {
                        Text("Название комании", style = MaterialTheme.typography.bodySmall)
                    },

                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface

                    )
                )
            }
            items(1) {
                OutlinedTextField(
                    value = pointBusiness,
                    onValueChange = { pointBusiness = it },
                    modifier = Modifier.fillMaxWidth(1f),
                    maxLines = 1,
                    placeholder = {
                        Text(
                            "Чем будет заниматься компания?",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    label = {
                        Text("Суть бизнеса", style = MaterialTheme.typography.bodySmall)
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface

                    ),

                    )
            }
            items(1) {
                OutlinedTextField(
                    value = auditoriumBusiness,
                    onValueChange = { auditoriumBusiness = it },
                    modifier = Modifier.fillMaxWidth(1f),
                    maxLines = 1,
                    placeholder = {
                        Text("Кто основные клиенты?", style = MaterialTheme.typography.bodySmall)
                    },
                    label = {
                        Text("Целевая аудитория", style = MaterialTheme.typography.bodySmall)
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface

                    )
                )
            }
            items(1) {
                OutlinedTextField(
                    value = advantagesBusiness,
                    onValueChange = { advantagesBusiness = it },
                    modifier = Modifier.fillMaxWidth(1f),
                    maxLines = 1,
                    placeholder = {
                        Text(
                            "Почему клиенты выберут именно вас?",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    label = {
                        Text(
                            "Конкурентные преимущества",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface

                    )
                )
            }
            items(1) {
                OutlinedTextField(
                    value = monetizationBusiness,
                    onValueChange = { monetizationBusiness = it },
                    modifier = Modifier.fillMaxWidth(1f),
                    maxLines = 1,
                    placeholder = {
                        Text("Как будет зарабатывать?", style = MaterialTheme.typography.bodySmall)
                    },
                    label = {
                        Text("Модель монетизации", style = MaterialTheme.typography.bodySmall)
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface

                    )
                )
            }
            items(1) {
                OutlinedTextField(
                    value = barriersAndSolutionsBusiness,
                    onValueChange = { barriersAndSolutionsBusiness = it },
                    modifier = Modifier.fillMaxWidth(1f),
                    maxLines = 1,
                    placeholder = {
                        Text("Какие главные риски?", style = MaterialTheme.typography.bodySmall)
                    },
                    label = {
                        Text("Барьеры и решения", style = MaterialTheme.typography.bodySmall)
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focus.clearFocus(force = true) }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface

                    )

                )
            }
            items(1) {

                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize(1f)
                ) {
                    if (!isLoading.value) {
                        Button(
                            onClick = {
                                scope.launch {
                                    addViewModel.getFullChatResponse(
                                        nameBusiness,
                                        pointBusiness,
                                        auditoriumBusiness,
                                        advantagesBusiness,
                                        monetizationBusiness,
                                        barriersAndSolutionsBusiness
                                    )
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier
                                .navigationBarsPadding()
                                .fillMaxWidth(1f),
                            enabled = !(nameBusiness.isEmpty() || pointBusiness.isEmpty() || auditoriumBusiness.isEmpty() || advantagesBusiness.isEmpty() || monetizationBusiness.isEmpty() || barriersAndSolutionsBusiness.isEmpty() || !isConnected),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                "Сгенерировать план",
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (isLoadingNavigate.value) {
                            navigation.popBackStack()
                        }
                    } else if (isLoading.value) {
                        Dialog(
                            onDismissRequest = {},
                            properties = DialogProperties()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize(1f)
                            ) {
                                val composition by rememberLottieComposition(
                                    LottieCompositionSpec.RawRes(
                                        if (MaterialTheme.colorScheme.background == BackgroundDark) {
                                            R.raw.loading_dark
                                        } else {
                                            R.raw.loading_light
                                        }
                                    )
                                )
                                LottieAnimation(
                                    composition,
                                    modifier = Modifier.fillMaxWidth(1f),
                                    iterations = LottieConstants.IterateForever
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}