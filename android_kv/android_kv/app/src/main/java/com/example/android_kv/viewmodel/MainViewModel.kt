package com.example.android_kv.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_kv.model.Article
import com.example.android_kv.model.Notification
import com.example.android_kv.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles


    private val auth = FirebaseAuth.getInstance()
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    init {
        getArticles()
        getUserNotifications()
    }

    fun refreshArticles() {
        getArticles()
        getUserNotifications()
    }

    private fun getArticles() {
        db.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                val articlesList = mutableListOf<Article>()
                for (document in result) {
                    val article = document.toObject(Article::class.java)
                    articlesList.add(article)
                }
                _articles.value = articlesList
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Greška u dohvaćanju dokumenata.", exception)
            }
    }

    private fun getUserNotifications() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val notificationsList = document.toObject(User::class.java)?.notifications ?: emptyList()
                        _notifications.value = notificationsList
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Greška u dohvaćanju obavijesti.", exception)
                }
        }
    }

    fun refreshNotifications(notification: Notification) {
        _notifications.value = _notifications.value?.filterNot { it == notification }
    }
}