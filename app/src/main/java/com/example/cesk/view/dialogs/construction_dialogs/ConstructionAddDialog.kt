package com.example.cesk.view.dialogs.construction_dialogs

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.cesk.model.enums.ConstructionType
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.model.enums.DialogType
import com.example.cesk.view.reusable_interface.UniversalButton
import com.example.cesk.ui.theme.Blue10
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Red10
import com.example.cesk.view.dialogs.EnduranceDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConstructionAddDialog(
    onCLick: () -> Unit = {},
    group: Group,
    construction: Construction,
    dialogType: DialogType,
    vm: ConstructionDialogViewModel = viewModel()
){
    if(dialogType == DialogType.EDIT){
        vm.loadState(construction)
    }
    val dialogState by vm.constructionDialogState.collectAsState()

    Dialog(
        onDismissRequest = {
            if(dialogType == DialogType.ADD){
                group.constructions.remove(construction)
            }
            onCLick()
            vm.flushState()
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
                    items(dialogState.testsList.size) {
                        Text(
                            text = dialogState.testsList[it].toString(),
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        vm.onTestDelete(it)
                                    }
                                )
                        )
                    }
                    item {
                        Text(
                            text = "Ср: " + "${Math.round(vm.getAverageEndurance() * 10.0) / 10.0}" + " МПа",
                            fontSize = 17.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                    item {
                        UniversalButton(
                            onClick = {
                                vm.onEnduranceAdd()
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
                            vm.onTypeSwitch()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        dialogState.constructionType.value?.let {
                            Text(
                                text = it,
                                color = Color.Black,
                                fontSize = 15.sp
                            )
                        }
                        DropdownMenu(
                            expanded = dialogState.typeMenuSwitch,
                            onDismissRequest = {
                                vm.onTypeSwitch()
                            }
                        ) {
                            ConstructionTypeDropDownItem(
                                type = ConstructionType.WALL,
                                typeTemp = {vm.onConstructionTypeChange(it)},
                                onClick = {
                                    vm.onTypeSwitch()
                                }
                            )
                            ConstructionTypeDropDownItem(
                                type = ConstructionType.PLATE,
                                typeTemp = {vm.onConstructionTypeChange(it)},
                                onClick = {
                                    vm.onTypeSwitch()
                                }
                            )
                            ConstructionTypeDropDownItem(
                                type = ConstructionType.COLUMN,
                                typeTemp = {vm.onConstructionTypeChange(it)},
                                onClick = {
                                    vm.onTypeSwitch()
                                }
                            )
                            ConstructionTypeDropDownItem(
                                type = ConstructionType.PYLON,
                                typeTemp = {vm.onConstructionTypeChange(it)},
                                onClick = {
                                    vm.onTypeSwitch()
                                }
                            )
                        }
                    }
                    TextField(
                        value = dialogState.constructionNote,
                        onValueChange = {vm.onConstructionNoteChange(it)},
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
                            construction.type = dialogState.constructionType
                            construction.note = dialogState.constructionNote
                            construction.averageEndurance = Math.round(vm.getAverageEndurance() * 10.0) / 10.0 //round to tenths
                            construction.tests = dialogState.testsList
                            if(dialogType == DialogType.ADD) {
                                group.constructions.add(construction)
                            }
                            onCLick()
                            vm.flushState()
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
                                vm.onConstructionDelete()
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
    if(dialogState.enduranceAddDialog){
        EnduranceDialog(
            onClick = {
                vm.onEnduranceAdd()
            },
            testsList = dialogState.testsList
        )
    }
    if(dialogState.constructionDeleteDialog){
        ConstructionDeleteDialog(
            group = group,
            construction = construction,
            onClick = {
                vm.onConstructionDelete()
            },
            constructionDialogClose = {
                onCLick()
                vm.flushState()
            }
        )
    }
}

@Composable
fun ConstructionTypeDropDownItem(
    type: ConstructionType,
    typeTemp: (ConstructionType) -> Unit,
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