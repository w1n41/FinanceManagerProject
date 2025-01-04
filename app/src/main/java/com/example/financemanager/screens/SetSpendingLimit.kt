package com.example.financemanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.financemanager.ui.theme.LightColorScheme
import com.example.financemanager.viewmodels.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetSpendingLimitDialog(
    viewModel: FinanceViewModel,
    navController: NavHostController
) {
    val spendingLimit = viewModel.spendingLimit.collectAsState()
    var textFieldValue by remember { mutableStateOf(spendingLimit.value.toString()) }

    AlertDialog(
        onDismissRequest = {
            viewModel.changeSetSpendingLimitStatus()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = LightColorScheme.onPrimary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Spending Limit") }
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    textFieldValue.toDoubleOrNull()?.let { doubleValue ->
                        viewModel.setSpendingLimit(doubleValue)
                        viewModel.changeSetSpendingLimitStatus()
                        navController.navigate(Screens.DASHBOARD)
                    }
                }
            ){
                Text("Set spending limit.")
            }
        }
    }
}