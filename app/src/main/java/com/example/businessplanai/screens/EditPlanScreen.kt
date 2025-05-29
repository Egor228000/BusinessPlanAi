package com.example.businessplanai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businessplanai.R
import com.example.businessplanai.viewModel.EditViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlan(
    editViewModel: EditViewModel,
    id: Int,
    scope: CoroutineScope,
    onBack: () -> Unit
) {
    val focus = LocalFocusManager.current
    LaunchedEffect(id) {
        editViewModel.loadBusinessById(id)
    }
    val business by editViewModel.business.collectAsState()
    LaunchedEffect(business?.id) {
        business?.let {
            editViewModel.editedTitle.value = it.title
            editViewModel.editedDescription.value = it.description
        }
    }
    val title by editViewModel.editedTitle
    val description by editViewModel.editedDescription

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
                Text(
                    text = stringResource(R.string.titleEdit),
                    color = MaterialTheme.colorScheme.background,
                )
            },
            actions = {
                IconButton(
                    onClick = {
                        scope.launch {
                            editViewModel.saveChanges()
                        }
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_done_24),
                        null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
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
        business?.let { it1 ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                items(1) {
                    Spacer(modifier = Modifier.padding(top = 16.dp))

                    OutlinedTextFieldCustom(
                        title,
                        { editViewModel.editedTitle.value = it },
                        stringResource(R.string.fieldCustomText_1),
                        focus
                    )

                }
                items(1) {

                    OutlinedTextField(
                        value = description,
                        onValueChange = { editViewModel.editedDescription.value = it },
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedBorderColor = Color(0x00FFFFFF),
                            focusedBorderColor = Color(0x00FFFFFF),
                        ),
                        placeholder = {
                            Text(
                                stringResource(R.string.fieldCustomTextEdit_2),
                                color = MaterialTheme.colorScheme.surface,
                                fontSize = 20.sp

                            )
                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 20.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focus.clearFocus(force = true) }
                        )

                    )
                }
            }
        }
    }
}