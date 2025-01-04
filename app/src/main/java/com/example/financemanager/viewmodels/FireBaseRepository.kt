package com.example.financemanager.viewmodels

import com.example.financemanager.data.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireBaseRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val username = auth.currentUser?.email ?: "Can't get email"

    private val transactionsCollection = firestore.collection("transactions")
    private val usersCollection = firestore.collection("users")

    private fun getCurrentUserId() = auth.currentUser?.uid

    suspend fun addTransaction(transaction: Transaction) {
        getCurrentUserId()?.let { userId ->
            val newTransaction = transaction.copy(
                userId = userId,
                date = System.currentTimeMillis()
            )

            transactionsCollection
                .add(newTransaction)
                .await()
        } ?: throw IllegalStateException("User not authenticated")
    }

    suspend fun getTransactions(): List<Transaction> {
        return getCurrentUserId()?.let { userId ->
            transactionsCollection
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    doc.toObject(Transaction::class.java)?.copy(id = doc.id)
                }
        } ?: emptyList()
    }

    suspend fun setSpendingLimit(limit: Double) = suspendCoroutine { continuation ->
        usersCollection.document(getCurrentUserId()!!)
            .set(mapOf("spendingLimit" to limit))
            .addOnSuccessListener {
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                e -> continuation.resumeWithException(e)
            }
    }

    suspend fun getSpendingLimit(): Double = suspendCoroutine { continuation ->
        usersCollection.document(getCurrentUserId()!!)
            .get()
            .addOnSuccessListener { document ->
                val limit = document?.getDouble("spendingLimit") ?: 0.0
                continuation.resume(limit)
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }
}