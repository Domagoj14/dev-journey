package com.example.android_kv.view

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.android_kv.R
import com.example.android_kv.model.Article
import com.example.android_kv.viewmodel.ArticleViewModel
import com.example.android_kv.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ArticleDetailsScreen(navController: NavController, articleId: String, viewModel: ArticleViewModel) {
    val article by viewModel.article.observeAsState()
    var ownerName by remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(articleId) {
        viewModel.getArticleById(articleId)
    }


    LaunchedEffect(article) {
        article?.let { fetchedArticle ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(fetchedArticle.ownerID)
                .get()
                .addOnSuccessListener { userDocument ->
                    if (userDocument != null) {
                        ownerName = userDocument.getString("name").toString()
                    } else {
                        Log.d(TAG, "Korisnik nije pronađen za ownerID: ${fetchedArticle.ownerID}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Pogreška u dohvaćanju detalja korisnika: $exception")
                }
        }
    }

    article?.let { fetchedArticle ->
        Column(modifier = Modifier.padding(8.dp)) {
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
                    text = fetchedArticle.itemName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            val painter = rememberImagePainter(data = fetchedArticle.imageUri)
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Vlasnik: ${ownerName}",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            )

            Text(
                text = "Lokacija ${fetchedArticle.location}",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            )

            Text(
                text = fetchedArticle.description,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (currentUser?.uid == fetchedArticle.ownerID) {
                Button(
                    onClick = { showDeleteDialog = true }, // Show dialog on click
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Obriši artikl", color = Color.White)
                }
            } else {
                Button(
                    onClick = { navController.navigate("enter_data/${article!!.id}") },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Pošalji zahtjev", color = Color.White)
                }
            }

            // Show confirmation dialog
            if (showDeleteDialog) {
                DeleteConfirmationDialog(
                    onConfirm = {
                        viewModel.deleteArticle(fetchedArticle.id) {
                            navController.navigate("main_screen")
                        }
                        showDeleteDialog = false
                    },
                    onDismiss = {
                        showDeleteDialog = false
                    }
                )
            }
        }
    } ?: run {
        Text(
            text = "No details",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Potvrda brisanja") },
        text = { Text(text = "Jeste li sigurni da želite obrisati ovaj artikl?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Da")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Ne")
            }
        }
    )
}