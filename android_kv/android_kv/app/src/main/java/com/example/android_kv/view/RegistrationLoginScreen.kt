package com.example.android_kv.view

import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.android_kv.viewmodel.AuthState
import com.example.android_kv.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegistrationLoginScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    val registrationState by remember { viewModel.registrationState }
    val loginState by remember { viewModel.loginState }

    LaunchedEffect(registrationState) {
        when (registrationState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Registracija uspješna", Toast.LENGTH_SHORT).show()
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    viewModel.createUserInFirestore(
                        userID = currentUser.uid,
                        userName = name,
                        userEmail = email
                    )
                }
                navController.navigate("main_screen") {
                    popUpTo("registration_login") { inclusive = true }
                }
            }
            is AuthState.Failure -> {
                Toast.makeText(context, "Registracija nije uspješna: ${(registrationState as AuthState.Failure).message}", Toast.LENGTH_LONG).show()
            }
            else -> { /* Do nothing */ }
        }
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Login uspješan", Toast.LENGTH_SHORT).show()
                navController.navigate("main_screen") {
                    popUpTo("registration_login") { inclusive = true }
                }
            }
            is AuthState.Failure -> {
                Toast.makeText(context, "Login nije uspješan: ${(loginState as AuthState.Failure).message}", Toast.LENGTH_LONG).show()
            }
            else -> { /* Do nothing */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Registration Section
        Text(
            text = "Registracija",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Ime") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "E-mail") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = confirmEmail,
            onValueChange = { confirmEmail = it },
            label = { Text(text = "Potvrdi E-mail") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Lozinka") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(text = "Potvrdi Lozinku") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                if (email == confirmEmail && password == confirmPassword) {
                    viewModel.registerUser(email, password)
                } else {
                    Toast.makeText(context, "Email-ovi ili lozinke se ne podudaraju", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Registriraj se")
        }

        Text(text = "ili", fontSize = 14.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Login Section
        Text(
            text = "Prijava",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = loginEmail,
            onValueChange = { loginEmail = it },
            label = { Text(text = "E-mail") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = loginPassword,
            onValueChange = { loginPassword = it },
            label = { Text(text = "Lozinka") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                viewModel.loginUser(loginEmail, loginPassword)
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Prijavi se")
        }
    }
}