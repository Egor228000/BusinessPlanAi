package com.example.businessplanai.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.businessplanai.viewModel.AddViewModel
import com.example.businessplanai.viewModel.SettingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.businessplanai.R

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AddPlan(
    padding: PaddingValues,
    addViewModel: AddViewModel,
    navigation: NavHostController,
    settingViewModel: SettingViewModel
) {
    // Для TExtFields
    var nameBusiness by remember { mutableStateOf("") }
    var pointBusiness by remember { mutableStateOf("") }
    var auditoriumBusiness by remember { mutableStateOf("") }
    var advantagesBusiness by remember { mutableStateOf("") }
    var monetizationBusiness by remember { mutableStateOf("") }
    var barriersAndSolutionsBusiness by remember { mutableStateOf("") }
    val focus = LocalFocusManager.current

    // Загрузка и включение анимации
    val isLoading = addViewModel.isLoading.collectAsState()
    val isLoadingNavigate = addViewModel.isLoadingNavigate.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    // ПРоверка подключения
    val isConnected by addViewModel.isConnected.collectAsState()
    val ipAdress by settingViewModel.serverIp.collectAsState()


    val resources = stringResource(R.string.business_plan_template)
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

    Box(
        modifier = Modifier
            .padding(padding)

            .padding(top = 16.dp)
            .padding(16.dp)
            .fillMaxSize()
    ) {


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .padding(bottom = 70.dp)

        ) {
            items(1) {
                OutlinedTextFieldCustom(
                    nameBusiness,
                    {nameBusiness = it},
                    stringResource(R.string.fieldCustomText_1),
                    focus
                )

            }
            items(1) {
                OutlinedTextFieldCustom(
                    pointBusiness,
                    {pointBusiness = it},
                    stringResource(R.string.fieldCustomText_2),

                    focus
                )
            }

            items(1) {
                OutlinedTextFieldCustom(
                    auditoriumBusiness,
                    {auditoriumBusiness = it},
                    stringResource(R.string.fieldCustomText_3),
                    focus
                )
            }
            items(1) {
                OutlinedTextFieldCustom(
                    advantagesBusiness,
                    {advantagesBusiness = it},
                    stringResource(R.string.fieldCustomText_4),
                    focus
                )
            }
            items(1) {
                OutlinedTextFieldCustom(
                    monetizationBusiness,
                    {monetizationBusiness = it},
                    stringResource(R.string.fieldCustomText_5),
                    focus
                )
            }
            items(1) {
                OutlinedTextFieldCustom(
                    barriersAndSolutionsBusiness,
                    {barriersAndSolutionsBusiness = it},
                    stringResource(R.string.fieldCustomText_6),
                    focus,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focus.clearFocus(true) }
                    )
                )
            }

        }
        Spacer(modifier = Modifier)
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            if (!isLoading.value) {
                val context = LocalContext.current
                val resources = context.resources
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
                                barriersAndSolutionsBusiness,
                                ipAdress,
                                resources
                            )
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
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
                        stringResource(R.string.buttonText),
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
                        CircularProgressIndicator(
                           color = MaterialTheme.colorScheme.onSurface,
                        )

                    }
                }
            }
        }
    }
}


@Composable
fun OutlinedTextFieldCustom(
    textValue: String,
    textOnValueChange: (String) -> Unit,
    textPlaceholder: String, focus: FocusManager,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = { focus.moveFocus(FocusDirection.Next) }
    )
) {
    OutlinedTextField(
        value = textValue,
        onValueChange = textOnValueChange,
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
                textPlaceholder,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 20.sp

            )
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.background,
            fontSize = 20.sp
        ),
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}