package com.example.android_kv
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_kv.view.ArticleDetailsScreen
import com.example.android_kv.view.BackgroundImage
import com.example.android_kv.view.CreateArticleScreen
import com.example.android_kv.view.EnterDataScreen
import com.example.android_kv.view.MainScreen
import com.example.android_kv.view.NotificationScreen
import com.example.android_kv.view.RegistrationLoginScreen
import com.example.android_kv.viewmodel.ArticleViewModel
import com.example.android_kv.viewmodel.AuthViewModel
import com.example.android_kv.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val mainViewModel: MainViewModel by viewModels()
    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


        setContent {
            val navController = rememberNavController()
            val authViewModel = remember { AuthViewModel(auth) }

            val startDestination = if (auth.currentUser == null) {
                "registration_login"
            } else {
                "main_screen"
            }

            NavHost(navController, startDestination = startDestination) {
                composable("registration_login") {
                    BackgroundImage()
                    RegistrationLoginScreen(navController, authViewModel)
                }
                composable("main_screen") {
                    BackgroundImage()
                    MainScreen(navController, mainViewModel, authViewModel)
                }
                composable("create_article"){
                    BackgroundImage()
                    CreateArticleScreen(navController, articleViewModel)
                }
                composable("enter_data/{articleId}",
                    arguments = listOf(navArgument("articleId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
                    BackgroundImage()
                    EnterDataScreen(navController, articleId, articleViewModel)
                }
                composable(
                    "article_details/{articleId}",
                    arguments = listOf(navArgument("articleId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
                    BackgroundImage()
                    ArticleDetailsScreen(navController, articleId, articleViewModel)
                }
                composable("notifications"){
                    BackgroundImage()
                    NotificationScreen(navController, mainViewModel, articleViewModel)
                }
            }
        }
    }
    override fun onStop() {
        super.onStop()
        // Ensure no UI operations are performed here
    }
    override fun onDestroy() {
        super.onDestroy()
        // Clean up any resources here
    }
}
