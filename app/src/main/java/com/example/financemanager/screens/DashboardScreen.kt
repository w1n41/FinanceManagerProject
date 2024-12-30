package com.example.financemanager.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.financemanager.data.Transaction
import com.example.financemanager.viewmodels.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: FinanceViewModel
) {
    val transactions = viewModel.transactions.collectAsState()
    val spendingLimit = 200f

    val totalSpent = transactions.value.sumOf { it.amount }.toFloat()
    val spentPercentage = if (spendingLimit > 0) ((totalSpent / spendingLimit) * -1) else 0f

    LaunchedEffect(
        Unit
    ) {
        viewModel.loadTransactions()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My finances") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screens.PROFILE) }) {
                        Icon(Icons.Default.Face, "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.ADDTRANSACTION) }
            ) {
                Icon(Icons.Default.Add, "Add transaction")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Spacer(Modifier.height(6.dp))
            CircularProgressBar(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                percentage = spentPercentage,
                int = spendingLimit.toInt()
            )
            Spacer(Modifier.height(10.dp))
            LazyColumn(Modifier.fillMaxSize()) {
                items(transactions.value) { transaction ->
                    TransactionItem(transaction)
                }
            }
            Log.d("Transactions", transactions.value.toString())
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "${transaction.amount} $",
                style = MaterialTheme.typography.titleMedium,
                color = if (transaction.amount < 0) Color.Red else Color.Green
            )
        }
    }
}

@Composable
fun CircularProgressBar(
    modifier: Modifier,
    percentage: Float,
    int: Int,
    fontSize: TextUnit = 12.sp,
    radius: Dp = 50.dp,
    color: Color = Color.Black,
    strokeWidth: Dp = 10.dp,
    animDuration: Int = 2000,
    animDelay: Int = 0,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        label = "Limit",
        animationSpec = tween(
            durationMillis = animDuration, delayMillis = animDelay
        )
    )
    LaunchedEffect(curPercentage) {
        animationPlayed = true
    }
    Box(
        modifier = modifier.size(radius * 3f), contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * curPercentage.value,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You have left: ",
                color = Color.Black,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.padding(2.dp))
            Text(
                text = (int - (curPercentage.value * int)).toInt()
                    .toString() + "/" + int.toString(),
                color = Color.Black,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
