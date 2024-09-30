package com.example.cesk

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
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
            PlanEditor()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CESKTheme {

    }
}