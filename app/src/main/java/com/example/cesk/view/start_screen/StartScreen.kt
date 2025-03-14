package com.example.cesk.view.start_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cesk.R
import com.example.cesk.navigation.Screen
import com.example.cesk.view.reusable_interface.RecentFilesBox
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Purple10
import com.example.cesk.logic.PvpFile

@Composable
fun StartScreen(
    navController: NavController,
    pvpFile: PvpFile
){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cesk_mipmap),
                    modifier = Modifier.size(250.dp),
                    contentDescription = null
                )
                Button(
                    onClick = {
                        pvpFile.setIndex(0)
                        pvpFile.setGroupList(mutableListOf())

                        navController.navigate(Screen.PlanEditor.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple10
                    )
                ) {
                    Text(
                        text = "Создать",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
            RecentFilesBox(
                onClick = {
                    navController
                        .navigate(Screen.PlanEditor.route)
                },
                pvpFile = pvpFile
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    CESKTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(300.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    LazyColumn (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        item{
                            Text(
                                text = "файлы отсутствуют...",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.Gray
                            )
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .width(300.dp)
                                    .padding(vertical = 15.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(15.dp),
                                border = BorderStroke(1.dp, Purple10)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Недавние файлы:",
                                        color = Color.Black,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier
                                            .padding(10.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}