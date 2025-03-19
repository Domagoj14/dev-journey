package com.example.android_kv.model

data class User(
    val id: String="",
    val name: String="",
    val email: String="",
    val notifications: List<Notification> = emptyList()
)
