package com.example.financemanager.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.example.financemanager.viewmodels.AuthViewModel
import com.example.financemanager.viewmodels.FinanceViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FinanceNavigation(
    navController: NavHostController,
    financeViewModel: FinanceViewModel,
    authViewModel: AuthViewModel
) {
    val startDestination =  if (FirebaseAuth.getInstance().currentUser != null) {
        Screens.DASHBOARD
    } else {
        Screens.LOGIN
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screens.LOGIN) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screens.DASHBOARD) {
            DashboardScreen(navController, financeViewModel)
        }
        composable(Screens.ADD_TRANSACTION) {
            AddTransactionScreen(navController, financeViewModel)
        }
        composable(Screens.PROFILE) {
            ProfileScreen(navController, financeViewModel)
        }
        dialog(Screens.SET_SPENDING_LIMIT_DIALOG){
            SetSpendingLimitDialog(financeViewModel, navController)
        }
    }
}

object Screens {
    const val LOGIN: String = "login"
    const val DASHBOARD: String = "dashboard"
    const val ADD_TRANSACTION: String = "add_transaction"
    const val PROFILE: String = "profile"
    const val SET_SPENDING_LIMIT_DIALOG = "set_spending_limit_dialog"
}