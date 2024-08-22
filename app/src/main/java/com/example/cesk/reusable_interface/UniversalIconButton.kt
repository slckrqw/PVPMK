package com.example.cesk.reusable_interface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cesk.R
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Green10

@Composable
fun UniversalButton(
    onClick: () -> Unit,
    iconRes: Int,
    containerColor: Color = Color.White
){
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .clip(RectangleShape)
            .size(50.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(0.dp)
    ){
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = if(containerColor != Color.White){
                Color.White
            }
            else{
                Color.Black
            },
            modifier = Modifier.size(25.dp)
        )
    }
    /*IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(5.dp)
            .size(50.dp)
            .clip(RoundedCornerShape(5.dp)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor
        )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = if(containerColor != Color.White){
                Color.White
            }
            else{
                Color.Black
            },
            modifier = Modifier
                .size(35.dp)
        )
    }*/
}

@Preview(showBackground = true)
@Composable
fun UniversalButtonPreview() {
    CESKTheme {
        UniversalButton(
            onClick = { /*TODO*/ },
            iconRes = R.drawable.delete_icon,
        )
    }
}