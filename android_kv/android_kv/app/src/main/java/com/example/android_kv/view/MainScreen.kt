package com.example.android_kv.view

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.android_kv.R
import com.example.android_kv.viewmodel.AuthViewModel
import com.example.android_kv.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel, authViewModel: AuthViewModel) {
    val articles by mainViewModel.articles.observeAsState(listOf())
    var userName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Sve kategorije") }
    var expanded by remember { mutableStateOf(false) }

    val filteredArticles = if (selectedCategory == "Sve kategorije") {
        articles
    } else {
        articles.filter { it.category == selectedCategory }
    }

    DisposableEffect(Unit) {

        mainViewModel.refreshArticles()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val firestore = FirebaseFirestore.getInstance()
            val userRef = firestore.collection("users").document(currentUser.uid)
            userRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    userName = documentSnapshot.getString("name") ?: ""
                }
            }.addOnFailureListener { e ->
                Log.e("MainScreen", "Greška u dohvaćanju podataka korisnika", e)
            }
        }
        onDispose { }
    }

    Column {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.applogo),
                        contentDescription = "GiftAway Logo",
                        modifier = Modifier
                            .weight(1f)
                            .size(54.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = {
                            navController.navigate("create_article")
                        }) {
                        Image(
                            painter = painterResource(id = R.drawable.pluslogo),
                            contentDescription = "Dodaj artikl",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate("notifications")
                        }) {
                        Image(
                            painter = painterResource(id = R.drawable.notificationlogo),
                            contentDescription = "Obavijesti",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Button(
                        onClick = {
                            authViewModel.signOutUser()
                            navController.navigate("registration_login")
                        },
                        colors = ButtonDefaults.buttonColors(Color.Black),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(text = "Odjava", color = Color.White)
                    }
                }
            },
        )

        Text(
            text = "Pozdrav $userName!",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(text = "Sortiraj po kategoriji:", fontSize = 16.sp)
            Box {
                Button(onClick = { expanded = true }) {
                    Text(text = selectedCategory)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    val categories = listOf("Sve kategorije","Elektronika", "Namještaj", "Kozmetika", "Odjevni predmet", "Hrana/piće", "Pribor", "Neodređeno")
                    categories.forEach { cat ->

                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                selectedCategory = cat
                                expanded = false
                            },
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(filteredArticles) { article ->
                ArticleCard(
                    imageUri = article.imageUri,
                    itemName = article.itemName,
                    location = article.location,
                    onDetailsClick = { navController.navigate("article_details/${article.id}") },
                    onSendRequestClick = { navController.navigate("enter_data/${article.id}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ArticleCard(
    imageUri: String,
    itemName: String,
    location: String,
    onDetailsClick: () -> Unit,
    onSendRequestClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.padding(8.dp)
    ) {
        Column {
            // Image section
            val painter = rememberImagePainter(data = imageUri)
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            // Text section
            Column(modifier = Modifier.padding(8.dp)) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = itemName, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Lokacija: ${location}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDetailsClick) {
                    Text(text = "Detalji artikla ->", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onSendRequestClick,
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Pošalji zahtjev", color = Color.White)
                }
            }
        }
    }
}
