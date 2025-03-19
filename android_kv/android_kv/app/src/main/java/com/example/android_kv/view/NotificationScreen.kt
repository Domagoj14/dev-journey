package com.example.android_kv.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android_kv.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.android_kv.model.Notification
import com.example.android_kv.viewmodel.ArticleViewModel
import com.example.android_kv.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NotificationScreen(navController: NavController, mainViewModel: MainViewModel, articleViewModel: ArticleViewModel) {
    val notifications = mainViewModel.notifications.observeAsState(emptyList())

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userID = currentUser?.uid ?: ""
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        if (currentUser != null) {
            val firestore = FirebaseFirestore.getInstance()
            val userRef = firestore.collection("users").document(currentUser.uid)
            userRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    userName = documentSnapshot.getString("name") ?: ""
                    userEmail = documentSnapshot.getString("email") ?: ""
                }
            }.addOnFailureListener { e ->
                Log.e("MainScreen", "Pogreška u dohvaćanju podataka korisnika", e)
            }
        }
        onDispose { }
    }

    Column(modifier = Modifier.padding(16.dp)) {
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
                text = "Obavijesti",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (notifications.value.isEmpty()) {
            Text(text = "Nema dostupnih obiavijesti.")
        } else {
            LazyColumn {
                items(notifications.value) { notification ->
                    NotificationItem(
                        notification = notification,
                        onAccept = {
                            articleViewModel.createNotificationForUser(notification.senderID, userID, "${userName} prihvaća Vaš zahtjev za preuzimanje artikla. Kontakt: ${userEmail}", false)
                            articleViewModel.deleteNotificationForUser(userID, notification)
                            mainViewModel.refreshNotifications(notification)
                        },
                        onReject = {
                            articleViewModel.createNotificationForUser(notification.senderID, userID, "${userName} odbija Vaš zahtjev za preuzimanje artikla.", false)
                            articleViewModel.deleteNotificationForUser(userID, notification)
                            mainViewModel.refreshNotifications(notification)
                        },
                        onRemove = {
                            articleViewModel.deleteNotificationForUser(userID, notification)
                            mainViewModel.refreshNotifications(notification)
                        }
                    )
                    Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onAccept: () -> Unit, onReject: () -> Unit, onRemove: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = notification.message,
                modifier = Modifier.weight(1f).padding(vertical = 4.dp)
            )
            if (notification.request) {
                IconButton(
                    onClick = { onAccept() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    // Green button with check icon
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Prihvati",
                        tint = Color.Green
                    )
                }
                IconButton(
                    onClick = { onReject() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    // Red button with X icon
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Odbij",
                        tint = Color.Red
                    )
                }
            } else {
                IconButton(
                    onClick = { onRemove() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    // Red button with X icon
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Ukloni",
                        tint = Color.Gray
                    )
                }
            }
        }
        Text(
            text = notification.time,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}