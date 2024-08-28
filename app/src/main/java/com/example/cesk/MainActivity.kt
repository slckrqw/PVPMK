package com.example.cesk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cesk.navigation.Screen
import com.example.cesk.plan_editor.PlanEditor
import com.example.cesk.start_screen.StartScreen
import com.example.cesk.ui.theme.CESKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CESKTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PVPMK()
                }
            }
        }
    }
}

@Composable
fun PVPMK(){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen.route,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition  = { fadeIn() },
        popExitTransition  = { fadeOut() }
    ) {
        composable(Screen.StartScreen.route){
            StartScreen(navController = navController)
        }
        composable(Screen.PlanEditor.route){
            PlanEditor(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CESKTheme {

    }
}