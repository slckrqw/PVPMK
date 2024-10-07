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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.R
import com.example.cesk.model.enums.ConstructType
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.model.enums.DialogType
import com.example.cesk.reusable_interface.UniversalButton
import com.example.cesk.ui.theme.Blue10
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Red10
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConstructionAddDialog(
    onCLick: () -> Unit = {},
    group: Group,
    construction: Construction,
    dialogType: DialogType,
    constructionDialogViewModel: ConstructionDialogViewModel = viewModel()
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
    val testsList = remember{
        construction.tests.toMutableStateList()
    }

    averageEndurance = if(testsList.isNotEmpty()){
        (testsList.sum()/testsList.size)
    }
    else 0.0

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
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 10.dp)
                        .width(120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(testsList.size) {
                        Text(
                            text = testsList[it].toString(),
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        testsList.removeAt(it)
                                    }
                                )
                        )
                    }
                    item {
                        Text(
                            text = "Ср: " + "${Math.round(averageEndurance * 10.0) / 10.0}" + " МПа",
                            fontSize = 17.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                    item {
                        UniversalButton(
                            onClick = {
                                constructionDialogViewModel
                                    .setEnduranceAddDialog(true)
                            },
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
                        onClick = {
                            constructionDialogViewModel
                                .setTypeMenuSwitch(true)
                        },
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
                            expanded = constructionDialogViewModel
                                .getTypeMenuSwitch(),
                            onDismissRequest = {
                                constructionDialogViewModel
                                    .setTypeMenuSwitch(false)
                            }
                        ) {
                            ConstructionTypeDropDownItem(
                                type = ConstructType.WALL,
                                typeTemp = {typeTemp = it},
                                onClick = {
                                    constructionDialogViewModel
                                        .setTypeMenuSwitch(false)
                                }
                            )
                            ConstructionTypeDropDownItem(
                                type = ConstructType.PLATE,
                                typeTemp = {typeTemp = it},
                                onClick = {
                                    constructionDialogViewModel
                                        .setTypeMenuSwitch(false)
                                }
                            )
                            ConstructionTypeDropDownItem(
                                type = ConstructType.COLUMN,
                                typeTemp = {typeTemp = it},
                                onClick = {
                                    constructionDialogViewModel
                                        .setTypeMenuSwitch(false)
                                }
                            )
                            ConstructionTypeDropDownItem(
                                type = ConstructType.PYLON,
                                typeTemp = {typeTemp = it},
                                onClick = {
                                    constructionDialogViewModel
                                        .setTypeMenuSwitch(false)
                                }
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
                            construction.averageEndurance = Math.round(averageEndurance * 10.0) / 10.0 //round to tenths
                            construction.tests = testsList.toMutableList()
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
                    if(dialogType == DialogType.EDIT){
                        Button(
                            onClick = {
                                constructionDialogViewModel
                                    .setConstructionDeleteDialog(true)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Red10),
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                                .fillMaxWidth()
                        ){
                            Text(
                                text = "Удалить",
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
    if(constructionDialogViewModel.getEnduranceAddDialog()){
        EnduranceDialog(
            onClick = {
                constructionDialogViewModel
                    .setEnduranceAddDialog(false)
            },
            testsList = testsList,
            construction = construction
        )
    }
    if(constructionDialogViewModel.getConstructionDeleteDialog()){
        ConstructionDeleteDialog(
            group = group,
            construction = construction,
            onClick = {
                constructionDialogViewModel
                    .setConstructionDeleteDialog(false)
            }
        )
    }
}

@Composable
fun ConstructionTypeDropDownItem(
    type: ConstructType,
    typeTemp: (ConstructType) -> Unit,
    onClick: () -> Unit
){
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
            onClick()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    CESKTheme {
        ConstructionAddDialog(group = Group(), construction = Construction(), dialogType = DialogType.EDIT)
    }
}