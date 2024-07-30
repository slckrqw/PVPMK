package com.example.cesk.reusable_interface.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cesk.R
import com.example.cesk.model.enums.ConstructType
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.model.enums.DialogType
import com.example.cesk.reusable_interface.UniversalButton
import com.example.cesk.ui.theme.Blue10
import com.example.cesk.ui.theme.CESKTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConstructionAddDialog(
    onCLick: () -> Unit = {},
    group: Group,
    construction: Construction,
    dialogType: DialogType
){

    var typeTemp by remember{
        mutableStateOf(ConstructType.NOTHING)
    }
    var noteTemp by remember{
        mutableStateOf(construction.note)
    }
    var averageEndurance by remember{
        mutableDoubleStateOf(0.0)
    }
    averageEndurance = if(construction.tests.isNotEmpty()){
        (construction.tests.sum()/construction.tests.size)
    }
    else 0.0

    var chooseEndurance by remember{
        mutableStateOf(false)
    }
    var typeMenuSwitch by remember{
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = {
            if(dialogType == DialogType.ADD){
                group.constructions.remove(construction)
            }
            onCLick()
        }
    ) {
        Card(
            modifier = Modifier
                .width(525.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 10.dp)
                        .width(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    construction.tests.forEach {
                        Text(
                            text = it.toString(),
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        construction.tests.remove(it)
                                    }
                                )
                        )
                    }
                    Text(
                        text = "Ср: " + averageEndurance.roundToInt().toString() + " МПа",
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row{
                        UniversalButton(
                            onClick = { chooseEndurance = true },
                            iconRes = R.drawable.plus_icon
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp, end = 5.dp, start = 10.dp)
                        .width(400.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { typeMenuSwitch = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        (if(typeTemp.value != null){
                            typeTemp.value
                        } else "Тип конструкции")?.let {
                            Text(
                                text = it,
                                color = Color.Black,
                                fontSize = 15.sp
                            )
                        }
                        DropdownMenu(
                            expanded = typeMenuSwitch,
                            onDismissRequest = { typeMenuSwitch = false }
                        ) {
                            ConstructionTypeDropDownItem(
                                type = ConstructType.STANDARD,
                                typeTemp = {typeTemp = it},
                                expanded = { typeMenuSwitch = it }
                            )
                            ConstructionTypeDropDownItem(
                                type = ConstructType.WALL,
                                typeTemp = {typeTemp = it},
                                expanded = { typeMenuSwitch = it }
                            )
                            ConstructionTypeDropDownItem(
                                type = ConstructType.PLATE,
                                typeTemp = {typeTemp = it},
                                expanded = { typeMenuSwitch = it }
                            )
                        }
                    }
                    TextField(
                        value = noteTemp,
                        onValueChange = {noteTemp = it},
                        placeholder = {
                            Text(
                                text = "Примечание",
                                fontSize = 15.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.comment_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    )
                    Button(
                        onClick = {
                            construction.type = typeTemp
                            construction.note = noteTemp
                            construction.averageEndurance = averageEndurance
                            onCLick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Blue10),
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = dialogType.value,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
    if(chooseEndurance){
        EnduranceDialog(
            expanded = {chooseEndurance = it},
            construction = construction
        )
    }
}

@Composable
fun ConstructionTypeDropDownItem(type: ConstructType, typeTemp: (ConstructType) -> Unit, expanded: (Boolean) -> Unit){
    DropdownMenuItem(
        text = {
            type.value?.let {
                Text(
                    text = it
                )
            }
        },
        modifier = Modifier.width(150.dp),
        onClick = {
            typeTemp(type)
            expanded(false)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    CESKTheme {
    }
}