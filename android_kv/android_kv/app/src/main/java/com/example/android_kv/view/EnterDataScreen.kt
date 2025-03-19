package com.example.android_kv.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android_kv.R
import com.example.android_kv.model.Article
import com.example.android_kv.viewmodel.ArticleViewModel
import com.example.android_kv.viewmodel.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EnterDataScreen(navController: NavController, articleId: String, articleViewModel: ArticleViewModel) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var surname by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userID = currentUser?.uid ?: ""

    val article by articleViewModel.article.observeAsState()
    LaunchedEffect(articleId) {
        articleViewModel.getArticleById(articleId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { navController.navigate("main_screen") },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backarrow),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Unesi podatke",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center).padding(10.dp)
            )
        }

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Ime (min 2. znaka)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Prezime (min 2. znaka)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Adresa (min. 10 znakova)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Broj mobitela (min 6. brojeva)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (articleViewModel.validateInput(name.text, surname.text, address.text, phoneNumber.text)) {
                    articleViewModel.createNotificationForUser(
                        article!!.ownerID,
                        senderID = userID,
                        "${name.text} ${surname.text} šalje vam zahtjev za preuzimanjem artikla ${article!!.itemName}. Adresa korisnika: ${address.text} Broj telefona: ${phoneNumber.text}",
                        true
                    )
                    navController.navigate("main_screen")
                } else {
                    Toast.makeText(context, "Neispravno unesene vrijednosti, pokušajte ponovno.", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Pošalji zahtjev", color = Color.White)
        }
    }
}