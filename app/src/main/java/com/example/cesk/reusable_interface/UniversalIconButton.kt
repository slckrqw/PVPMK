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
    isActive: Boolean = false
){
    /*Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp))
            .size(30.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        contentPadding = PaddingValues(3.dp),
        border = BorderStroke(1.dp, color = Color.Black)
    ){
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = when(isActive){
                true -> Green10
                false -> Color.Black
            },
            modifier = Modifier.size(25.dp)
        )
    }*/
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(5.dp)
            .size(50.dp)
            .clip(RoundedCornerShape(5.dp)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = when(isActive){
                true -> Green10
                false -> Color.White
            }
        )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = when(isActive){
                true -> Color.White
                false -> Color.Black
            },
            modifier = Modifier
                .size(35.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UniversalButtonPreview() {
    CESKTheme {
        UniversalButton(
            onClick = { /*TODO*/ },
            iconRes = R.drawable.delete_icon,
            isActive = true
        )
    }
}