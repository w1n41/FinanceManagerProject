package com.example.financemanager.viewmodels

import com.example.financemanager.data.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FinanceRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getCurrentUser() = auth.currentUser

    suspend fun addTransaction(transaction: Transaction) {
        getCurrentUser()?.let { user ->
            val transactionWithUser = transaction.copy(userId = user.uid)
            firestore.collection("transactions")
                .add(transactionWithUser)
                .await()
        } ?: throw IllegalStateException("User not authenticated")
    }

    suspend fun getTransactions(): List<Transaction> {
        return firestore.collection("transactions")
            .whereEqualTo("userId", getCurrentUser()?.uid)
            .get()
            .await()
            .toObjects(Transaction::class.java)
    }
}