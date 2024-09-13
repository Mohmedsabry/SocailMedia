@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.socailmedia.util.ConstObject.FEMALE
import com.example.socailmedia.util.ConstObject.MALE

@Composable
fun DropDown(
    modifier: Modifier = Modifier,
    items: List<String> = listOf(MALE, FEMALE),
    isError: Boolean,
    onValueChange: (String) -> Unit
) {
    var expended by remember {
        mutableStateOf(false)
    }
    var selectedItem by remember {
        mutableStateOf(items[0])
    }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expended,
            onExpandedChange = { expended = expended.not() },
            modifier = modifier
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                isError = isError,
                modifier = modifier.menuAnchor(),
                label = { Text(text = "select") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expended) }
            )
            ExposedDropdownMenu(
                expanded = expended,
                onDismissRequest = { expended = false }
            ) {
                items.forEach {
                    DropdownMenuItem(text = {
                        Text(text = it)
                    }, onClick = {
                        onValueChange(it)
                        selectedItem = it
                        expended = false
                    })
                }
            }
        }
    }
}
