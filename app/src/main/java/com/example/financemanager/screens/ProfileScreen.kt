package com.example.financemanager.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.financemanager.ui.theme.LightColorScheme
import com.example.financemanager.viewmodels.FinanceViewModel
import com.example.financemanager.viewmodels.FireBaseRepository
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: FinanceViewModel
) {
    val transactions = viewModel.transactions.collectAsState()
    val firebase = FireBaseRepository()
    val spendingLimitStatus = viewModel.settingSpendingLimit.collectAsState()
    val spendingLimit = viewModel.spendingLimit.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Profile") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
            actions = {
                Button(
                    modifier = Modifier.height(35.dp).width(100.dp),
                    onClick = {
                        try {
                            com.google.firebase.Firebase.auth.signOut()
                            navController.navigate(Screens.LOGIN){
                                popUpTo(navController.graph.startDestinationId){
                                    inclusive = true
                                }
                            }
                        }
                        catch (e: Exception){
                            Log.e("Exception", e.message.toString())
                        }
                    },
                    colors = ButtonColors(Color.Red, Color.White, Color.DarkGray, Color.White)
                ){
                    Text("Logout")
                }
            }
        )

        val categoryTotals = transactions.value
            .groupBy { it.category }
            .mapValues { (_, transactions) ->
                transactions.sumOf { it.amount }
            }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(firebase.username)
        }

        Spacer(Modifier.height(20.dp))

        if(spendingLimit.value.toInt() != 0){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        viewModel.changeSetSpendingLimitStatus()
                    },
                    colors = ButtonColors(
                        LightColorScheme.primary,
                        LightColorScheme.onPrimary,
                        LightColorScheme.secondary,
                        LightColorScheme.onSecondary
                    )
                ) {
                    Text(
                        text = "Change spending limit."
                    )
                }
                if (spendingLimitStatus.value){
                    navController.navigate(Screens.SET_SPENDING_LIMIT_DIALOG)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row {
            Text("Your total: ")
        }

        Spacer(Modifier.height(20.dp))

        LazyColumn {
            items(categoryTotals.toList()) { (category, total) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(category)
                        Text(
                            "$total $",
                            color = if (total < 0) Color.Red else Color.Green
                        )
                    }
                }
            }
        }
    }
}