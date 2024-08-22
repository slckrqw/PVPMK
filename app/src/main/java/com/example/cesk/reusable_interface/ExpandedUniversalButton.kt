package com.example.cesk.reusable_interface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cesk.R
import com.example.cesk.ui.theme.CESKTheme

@Composable
fun ExpandedUniversalButton(
    onClick: () -> Unit,
    iconRes: Int,
    text: String,
    containerColor: Color = Color.White
){
    val contentColor = if(containerColor!= Color.White){
        Color.White
    }
    else{
        Color.Black
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .height(50.dp)
            .width(200.dp)
            .clip(RectangleShape),
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        )
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .size(25.dp)
            )
            Text(
                text = text,
                modifier = Modifier.padding(5.dp),
                color = contentColor,
                fontSize = 17.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandedUniversalButtonPreview() {
    CESKTheme {
        ExpandedUniversalButton(
            onClick = { /*TODO*/ },
            iconRes = R.drawable.delete_icon,
            text = "Удалить"
        )
    }
}