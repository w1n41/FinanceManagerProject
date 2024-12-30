package com.example.financemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinanceViewModel : ViewModel() {
    private val repository = FireBaseRepository()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions = _transactions.asStateFlow()

    private val _spendingLimit = MutableStateFlow(0.0)
    val spendingLimit = _spendingLimit.asStateFlow()

    init {
        loadTransactions()
        loadSpendingLimit()
    }

    fun addTransaction(amount: Double, category: String, description: String) {
        viewModelScope.launch {
            try {
                val transaction = Transaction(
                    amount = amount,
                    category = category,
                    description = description
                )
                repository.addTransaction(transaction)
                loadTransactions()
            } catch (e: Exception) {
                val toast = e.message
            }
        }
    }

    fun loadTransactions() {
        viewModelScope.launch {
            try {
                _transactions.value = repository.getTransactions().toMutableList()
            } catch (e: Exception) {
                val toast = e.message
            }
        }
    }

    fun setSpendingLimit(limit: Double) {
        viewModelScope.launch {
            try {
                repository.setSpendingLimit(limit)
                _spendingLimit.value = limit
            } catch (e: Exception) {
                val toast = e.message
            }
        }
    }

    private fun loadSpendingLimit() {
        viewModelScope.launch {
            try {
                _spendingLimit.value = repository.getSpendingLimit()
            } catch (e: Exception) {
                val toast = e.message
            }
        }
    }
}