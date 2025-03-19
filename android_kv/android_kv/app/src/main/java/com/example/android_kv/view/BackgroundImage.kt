package com.example.android_kv.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.android_kv.R

@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.backgoundimg),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.05F,
        modifier = Modifier.fillMaxSize()
    )
}