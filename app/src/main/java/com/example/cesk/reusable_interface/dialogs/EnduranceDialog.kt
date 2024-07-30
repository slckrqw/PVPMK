package com.example.cesk.reusable_interface.dialogs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cesk.R
import com.example.cesk.model.Construction
import com.example.cesk.ui.theme.CESKTheme

@Composable
fun EnduranceDialog(expanded: (Boolean) -> Unit = {}, construction: Construction = Construction()){
    var endurance by remember{
        mutableStateOf("")
    }
    /*Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(

        ) {
            Column {
                EnduranceRow(from = 1, to = 10)
                EnduranceRow(from = 11, to = 20)
                EnduranceRow(from = 21, to = 30)
                EnduranceRow(from = 31, to = 40)
                EnduranceRow(from = 41, to = 50)
                EnduranceRow(from = 51, to = 60)
            }
        }
    }*/
    Dialog(onDismissRequest = { expanded(false) }) {
        TextField(
            value = endurance,
            onValueChange = {endurance = it},
            leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.endurance_icon),
                        modifier = Modifier.size(30.dp),
                        contentDescription = null
                    )
            },
            placeholder = {
                Text(
                    text = "Прочность в МПа",
                    fontSize = 15.sp
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                .copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if(endurance != "") {
                        construction.tests.add(endurance.toDouble())
                    }
                    expanded(false)
                }
            )
        )
    }
}

@Composable
fun EnduranceRow(from: Int, to: Int){
    Row {
        for (i in from..to) {
            Button(
                onClick = { /*TODO*/ },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(50.dp),
                shape = RectangleShape
            ) {
                Text(
                    text = i.toString(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EndurancePreview() {
    CESKTheme {
        EnduranceDialog()
    }
}