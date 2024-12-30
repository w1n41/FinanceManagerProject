package com.example.financemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.financemanager.screens.FinanceNavigation
import com.example.financemanager.ui.theme.FinanceManagerTheme
import com.example.financemanager.viewmodels.AuthViewModel
import com.example.financemanager.viewmodels.FinanceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanceManagerTheme {

                val navController = rememberNavController()
                val financeViewModel: FinanceViewModel = viewModel()
                val authViewModel: AuthViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FinanceNavigation(navController, financeViewModel, authViewModel)
                }
            }
        }
    }
}

