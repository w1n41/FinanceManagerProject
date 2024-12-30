package com.example.financemanager.screens

import android.provider.ContactsContract.Profile
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.financemanager.viewmodels.AuthViewModel
import com.example.financemanager.viewmodels.FinanceViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.Serial

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
        composable(Screens.ADDTRANSACTION) {
            AddTransactionScreen(navController, financeViewModel)
        }
        composable(Screens.PROFILE) {
            ProfileScreen(navController, financeViewModel)
        }
    }
}

object Screens {
    const val LOGIN: String = "login"
    const val DASHBOARD: String = "dashboard"
    const val ADDTRANSACTION: String = "add_transaction"
    const val PROFILE: String = "profile"
}