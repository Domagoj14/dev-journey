package com.example.android_kv.view

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.android_kv.R
import com.example.android_kv.model.Article
import com.example.android_kv.viewmodel.ArticleViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import java.util.UUID
import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter


@Composable
fun CreateArticleScreen(navController: NavController, articleViewModel: ArticleViewModel) {
    val context = LocalContext.current

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userID = currentUser?.uid ?: ""

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    var itemName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Dopuštenje odobreno", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Dopuštenje odbijeno", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                text = "Objavi artikl",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            if (capturedImageUri != Uri.EMPTY) {
                Image(
                    painter = rememberImagePainter(capturedImageUri),
                    contentDescription = "Uslikaj fotografiju",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                )
            }

            Button(
                onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp)
            ) {
                Text("Uslikaj artikl")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(6.dp)
        ) {
            Text("Kategorija:", modifier = Modifier.align(Alignment.CenterVertically))

            Box {
                Button(onClick = { expanded = true }) {
                    Text(text = if (category.isEmpty()) "Odaberi kategoriju" else category)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    val categories = listOf("Elektronika", "Namještaj", "Kozmetika", "Odjevni predmet", "Hrana/piće", "Pribor", "Neodređeno")
                    categories.forEach { cat ->

                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                expanded = false
                            },
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = itemName,
            onValueChange = {
                itemName = it
            },
            label = { Text("Ime artikla (min. 2 znaka)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = location,
            onValueChange = {
                location = it
            },
            label = { Text("Lokacija (min. 10 znakova)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = { Text("Opis (min. 10 znakova)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (capturedImageUri == Uri.EMPTY) {
                    Toast.makeText(context, "Nije unesena fotografija", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (itemName.length < 2) {
                    Toast.makeText(context, "Ime mora biti barem 2 znaka", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (location.length < 10) {
                    Toast.makeText(context, "Lokacija mora biti barem 10 znakova", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (description.length < 10) {
                    Toast.makeText(context, "Opis mora biti barem 10 znakova", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (category.isEmpty()) {
                    Toast.makeText(context, "Kategorija nije odabrana", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val uniqueID = UUID.randomUUID().toString()
                // Create a new article with the ownerID
                val newArticle = Article(
                    id = uniqueID,
                    imageUri = capturedImageUri.toString(),
                    itemName = itemName,
                    location = location,
                    description = description,
                    category = category,
                    ownerID = userID
                )
                articleViewModel.addArticle(newArticle, context, "Uspješno objavljen artikl ${itemName}!", navController, senderID = userID)
            },
            colors = ButtonDefaults.buttonColors(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Objavi artikl")
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )

    return image
}

