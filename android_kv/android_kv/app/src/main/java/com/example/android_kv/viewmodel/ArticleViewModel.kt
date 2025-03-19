package com.example.android_kv.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.android_kv.model.Article
import com.example.android_kv.model.Notification
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ArticleViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun addArticle(
        article: Article,
        context: Context,
        message: String,
        navController: NavController,
        senderID: String,
    ) {
        db.collection("articles").document(article.id).set(article)
            .addOnSuccessListener {
                // Handle success
                Toast.makeText(context, "Uspješno kreiran artikl!", Toast.LENGTH_SHORT).show()
                createNotificationForUser(article.ownerID, senderID, message, false)
                navController.popBackStack()
            }
            .addOnFailureListener { e ->
                // Handle failure
                Toast.makeText(context, "Pogreška u kreiranju artikla: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteArticle(articleId: String, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("articles").document(articleId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Article successfully deleted!")
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting article", e)
            }
    }


    private val _article = MutableLiveData<Article?>()
    val article: LiveData<Article?> = _article

    fun getArticleById(articleId: String) {
        db.collection("articles").document(articleId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val article = document.toObject(Article::class.java)
                    _article.value = article
                } else {
                    Log.d(TAG, "Ne postoji takav dokument")
                    _article.value = null
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Greška u dohvaćanju dokumenta", exception)
                _article.value = null
            }
    }

    fun createNotificationForUser(userId: String, senderID: String, message: String, isRequest: Boolean) {
        val notification = Notification(id = db.collection("users").document().id, senderID = senderID, message = message, request = isRequest)

        db.collection("users").document(userId)
            .update("notifications", FieldValue.arrayUnion(notification))
            .addOnSuccessListener {
                // Handle success
                println("Notifikacija uspješno kreirana")
            }
            .addOnFailureListener { e ->
                // Handle failure
                println("Pogreška u kreiranju obavijesti: ${e.message}")
            }
    }

    fun deleteNotificationForUser(userId: String, notification: Notification) {
        db.collection("users").document(userId)
            .update("notifications", FieldValue.arrayRemove(notification))
            .addOnSuccessListener {
                // Handle success
                println("Obavijest uspješno obrisana")
            }
            .addOnFailureListener { e ->
                // Handle failure
                println("Pogreška u brisanju obavijesti: ${e.message}")
            }
    }



    fun validateInput(name: String, surname: String, address: String, phoneNumber: String): Boolean {
        return name.isNotBlank() && name.length >= 2 &&
                surname.isNotBlank() && surname.length >= 2 &&
                address.isNotBlank() && address.length >= 10 &&
                phoneNumber.isNotBlank() && phoneNumber.isNumeric() && phoneNumber.length >= 6
    }

    private fun String.isNumeric(): Boolean {
        return all { it.isDigit() }
    }
}