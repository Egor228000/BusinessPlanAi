package com.example.businessplanai.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
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
import com.example.businessplanai.R
import com.example.businessplanai.ui.theme.BackgroundDark
import com.example.businessplanai.ui.theme.TextColorDark
import com.example.businessplanai.viewModel.AddViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        modifier = Modifier
            .padding(padding)
            .padding(top = 16.dp)

            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
        ) {
            items(1) {
                OutlinedTextField(
                    value = nameBusiness,
                    onValueChange = { nameBusiness = it },
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
                            "Названии компании",
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
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                )

            }
            items(1) {
                OutlinedTextField(
                    value = pointBusiness,
                    onValueChange = { pointBusiness = it },
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
                            "Суть бизнеса",
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
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                )
            }

            items(1) {
                OutlinedTextField(
                    value = auditoriumBusiness,
                    onValueChange = { auditoriumBusiness = it },
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
                            "Целевая аудитория",
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
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                )

            }
            items(1) {
                OutlinedTextField(
                    value = advantagesBusiness,
                    onValueChange = { advantagesBusiness = it },
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
                            "Конкурентные преимущества",
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
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                )

            }
            items(1) {
                OutlinedTextField(
                    value = monetizationBusiness,
                    onValueChange = { monetizationBusiness = it },
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
                            "Как будете зарабатывать?",
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
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                )


            }
            items(1) {
                OutlinedTextField(
                    value = barriersAndSolutionsBusiness,
                    onValueChange = { barriersAndSolutionsBusiness = it },
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
                            "Как будете зарабатывать?",
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
                )

            }

        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            if (!isLoading.value) {
                Button(
                    elevation = ButtonDefaults.buttonElevation(1.dp),
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
                    colors = ButtonDefaults.outlinedButtonColors(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(50.dp)
                        .fillMaxWidth(1f),
                    enabled = !(nameBusiness.isEmpty() || pointBusiness.isEmpty() || auditoriumBusiness.isEmpty() || advantagesBusiness.isEmpty() || monetizationBusiness.isEmpty() || barriersAndSolutionsBusiness.isEmpty() || !isConnected),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "Сгенерировать план",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 18.sp
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