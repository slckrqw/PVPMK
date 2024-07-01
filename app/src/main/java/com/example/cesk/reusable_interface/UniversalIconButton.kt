package com.example.cesk.reusable_interface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun UniversalButton(onClick: () -> Unit, iconRes: Int){
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(5.dp)
            .size(30.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(1.dp, color = Color.Black)
    ){
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.Black
        )
    }
}