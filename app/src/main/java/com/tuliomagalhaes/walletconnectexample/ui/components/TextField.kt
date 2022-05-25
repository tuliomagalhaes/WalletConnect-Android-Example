package com.tuliomagalhaes.walletconnectexample.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TextField(
    value: String,
    label: String,
    placeholder: String,
    onValueChanged: (String) -> Unit,
) {
    val text = remember { mutableStateOf(value) }

    OutlinedTextField(
        value = text.value,
        onValueChange = { newValue ->
            text.value = newValue
            onValueChanged(newValue)
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Ascii,
        )
    )
}