package com.example.android_kv.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_kv.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(private val auth: FirebaseAuth) : ViewModel() {

    val registrationState: MutableState<AuthState> = mutableStateOf(AuthState.Idle)
    val loginState: MutableState<AuthState> = mutableStateOf(AuthState.Idle)

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                registrationState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, password).await()
                registrationState.value = AuthState.Success
            } catch (e: Exception) {
                registrationState.value = AuthState.Failure(e.message ?: "Unknown Error")
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                loginState.value = AuthState.Loading
                auth.signInWithEmailAndPassword(email, password).await()
                loginState.value = AuthState.Success
            } catch (e: Exception) {
                loginState.value = AuthState.Failure(e.message ?: "Error")
            }
        }
    }
    fun createUserInFirestore(userID: String, userName: String, userEmail: String) {
        val user = User(id = userID, name = userName, email = userEmail, notifications = emptyList())

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users")
            .document(userID)
            .set(user)
            .addOnSuccessListener {
                println("Korisnikov document uspješno kreiran!")
            }
            .addOnFailureListener { e ->
                println("Greška pri kreiranju korisnikovog dokumenta: $e")
            }
    }

    fun signOutUser() {
        auth.signOut()
        loginState.value = AuthState.LoggedOut
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data object LoggedOut : AuthState()
    data class Failure(val message: String) : AuthState()
}