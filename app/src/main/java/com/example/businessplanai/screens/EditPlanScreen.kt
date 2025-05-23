package com.example.businessplanai.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.businessplanai.AppDatabase
import com.example.businessplanai.viewModel.EditViewModel

@Composable
fun EditPlan(
    padding: PaddingValues,
    editViewModel: EditViewModel,
    navigation: NavHostController,
    id: Int?
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

    business?.let { it1 ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .padding(padding)
        ) {
            items(1) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { editViewModel.editedTitle.value = it },
                    modifier = Modifier.fillMaxSize(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface

                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focus.moveFocus(focusDirection = FocusDirection.Next) }
                    ),
                    label = {
                        Text("Название")
                    }
                )
            }
            items(1) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { editViewModel.editedDescription.value = it },
                    modifier = Modifier.fillMaxSize(1f),
                    textStyle = MaterialTheme.typography.bodySmall,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focus.clearFocus(force = true) }
                    ),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface

                    ),
                    label = {
                        Text("Бизнес план")
                    }
                )
            }
        }
    }
}