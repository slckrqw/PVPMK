package com.example.cesk.start_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cesk.R
import com.example.cesk.navigation.Screen
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Purple10

@Composable
fun StartScreen(
    navController: NavController
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.cesk_mipmap),
                modifier = Modifier.size(250.dp),
                contentDescription = null
            )
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple10
                )
            ) {
                Text(
                    text = "Начать",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    CESKTheme {

    }
}