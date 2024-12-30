package com.example.cesk.view.groups_card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.R
import com.example.cesk.model.Group
import com.example.cesk.model.enums.DialogType
import com.example.cesk.view.reusable_interface.ExpandedUniversalButton
import com.example.cesk.view.reusable_interface.UniversalButton
import com.example.cesk.ui.theme.Green10
import com.example.cesk.ui.theme.Purple10
import com.example.cesk.view.dialogs.GroupDialog

@Composable
fun GroupsCard(
    onClick: () -> Unit,
    vm: GroupsCardViewModel = viewModel(),
    groupList: List<Group>,
    index: Int,
    setIndex: (Int) -> Unit,
    deleteGroup: () -> Unit
){
    val state by vm.groupsCardState.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp),
        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        LazyColumn {
            item{
                Row(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Purple10),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    UniversalButton(
                        onClick = {
                            onClick()
                        },
                        iconRes = R.drawable.right_arrow_icon,
                        containerColor = Purple10
                    )
                }
            }
            item{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    ExpandedUniversalButton(
                        onClick = {
                            vm.setGroupDialogType(DialogType.ADD)
                            vm.onGroupDialogChange()
                        },
                        iconRes = R.drawable.plus_icon,
                        text = "Добавить"
                    )
                    Text(
                        text = "Группы:",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
            if(groupList.isEmpty()){
                item{
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = "Их пока нет :(",
                            fontSize = 20.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            else {
                items(groupList) { group ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(
                            1.dp,
                            if (group.index == index) {
                                Green10
                            } else Color.LightGray
                        ),
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    setIndex(group.index)
                                    /*currentGroup = groupVM
                                        .getGroupList()
                                        .first {
                                            it.index == index
                                        }*/
                                },
                            )
                            .height(60.dp)
                            .fillMaxWidth(),
                        shape = RectangleShape
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    setIndex(group.index)
                                    vm.onGroupOptionsChange()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.dots),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                    tint = Color.Gray
                                )
                            }
                            Text(
                                text = group.name,
                                fontSize = 17.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(end = 10.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        DropdownMenu(
                            expanded = state.groupOptions && group.index == index,
                            onDismissRequest = {
                                vm.onGroupOptionsChange()
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Редактировать"
                                    )
                                },
                                onClick = {
                                    setIndex(group.index)
                                    vm.setGroupDialogType(DialogType.EDIT)
                                    vm.onGroupOptionsChange()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Удалить"
                                    )
                                },
                                onClick = {
                                    setIndex(group.index)
                                    deleteGroup()
                                    vm.onGroupOptionsChange()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    if(state.groupDialog){
        GroupDialog(
            onClick = {vm.onGroupDialogChange()},
            dialogType = state.groupDialogType,
            groupViewModel = groupVM
        )
    }
}