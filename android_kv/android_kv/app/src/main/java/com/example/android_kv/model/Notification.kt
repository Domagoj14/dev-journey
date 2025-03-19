package com.example.android_kv.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Notification(
    val id: String = "",
    val senderID: String = "",
    val message: String = "",
    val request: Boolean = false,
    val time: String = getFormattedDateTime(System.currentTimeMillis())
)

fun getFormattedDateTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}
