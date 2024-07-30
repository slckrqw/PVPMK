package com.example.cesk.reusable_interface.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Green10
import com.example.cesk.ui.theme.Red10

@Composable
fun ConstructionDeleteDialog(
    group: Group = Group(),
    construction: Construction = Construction(),
    onClick: () -> Unit = {}
){
    Dialog(
        onDismissRequest = {
            onClick()
        }
    ) {
       Card(
            colors = CardDefaults
                .cardColors(containerColor = Color.White),
            modifier = Modifier.shadow(1.dp)
       ){
           Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
           ){
               Text(
                   text = "Удалить конструкцию?",
                   fontSize = 25.sp,
                   color = Color.Black,
                   modifier = Modifier.padding(bottom = 10.dp)
               )
               Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
               ){
                   AgreeButton(
                       onClick = {
                             group.constructions.remove(construction)
                             onClick()
                       },
                       text = "Да",
                       containerColor = Green10
                   )
                   AgreeButton(
                       onClick = { onClick() },
                       text = "Нет",
                       containerColor = Red10
                   )
               }
           }
       }
    }
}

@Composable
fun RowScope.AgreeButton(
    onClick: () -> Unit,
    text: String,
    containerColor: Color
){
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults
            .buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier
            .padding(5.dp)
            .weight(1f)
    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConstructionDeletePreview() {
    CESKTheme {
        ConstructionDeleteDialog()
    }
}