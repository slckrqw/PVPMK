package com.example.cesk.view.tools_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.R
import com.example.cesk.model.enums.FileAccessType
import com.example.cesk.ui.theme.Purple10
import com.example.cesk.view.dialogs.FileAccessDialog
import com.example.cesk.view.reusable_interface.ExpandedUniversalButton
import com.example.cesk.view.reusable_interface.UniversalButton

@Composable
fun ToolsCard(
    openGroupsCard: () -> Unit,
    savePDF: () -> Unit,
    changePointsVisibility: () -> Unit,
    pointsVisibility: Boolean,
    vm: ToolsCardViewModel = viewModel()
){
    fun openFileDialog(dialogType: FileAccessType){
        vm.setFileAccessType(dialogType)
        vm.onFileDialogChange()
    }

    val state by vm.toolsCardState.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(50.dp),
        shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            when (state.toolsCardView) {
                false -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .background(Purple10)
                    ) {
                        UniversalButton(
                            onClick = {
                                vm.onToolsCardViewChange()
                            },
                            iconRes = R.drawable.right_arrow_icon,
                            containerColor = Purple10
                        )
                    }
                    UniversalButton(
                        onClick = {
                            openGroupsCard()
                        },
                        iconRes = R.drawable.menu
                    )
                    UniversalButton(
                        onClick = {
                            savePDF()
                        },
                        iconRes = R.drawable.pdf_convert
                    )
                    UniversalButton(
                        onClick = {
                            openFileDialog(FileAccessType.CNC)
                        },
                        iconRes = R.drawable.cnc_convert
                    )
                    UniversalButton(
                        onClick = {
                            openFileDialog(FileAccessType.SAVE)
                        },
                        iconRes = R.drawable.save_as_pdf_icon
                    )
                    UniversalButton(
                        onClick = {
                            openFileDialog(FileAccessType.OPEN)
                        },
                        iconRes = R.drawable.open_file
                    )
                    Button(
                        onClick = {
                            changePointsVisibility()
                        },
                        modifier = Modifier
                            .clip(RectangleShape)
                            .size(50.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = when (pointsVisibility) {
                                true -> painterResource(id = R.drawable.view)
                                false -> painterResource(id = R.drawable.hide)
                            },
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                }

                true -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .background(Purple10)
                    ) {
                        ExpandedUniversalButton(
                            onClick = {
                                vm.onToolsCardViewChange()
                            },
                            iconRes = R.drawable.left_arrow,
                            text = "Свернуть",
                            containerColor = Purple10
                        )
                    }
                    ExpandedUniversalButton(
                        onClick = {
                            openGroupsCard()
                        },
                        iconRes = R.drawable.menu,
                        text = "Список групп"
                    )
                    ExpandedUniversalButton(
                        onClick = {
                            savePDF()
                        },
                        iconRes = R.drawable.pdf_convert,
                        text = "Экспорт в PDF"
                    )
                    ExpandedUniversalButton(
                        onClick = {
                            openFileDialog(FileAccessType.CNC)
                        },
                        iconRes = R.drawable.cnc_convert,
                        text = "Экспорт в CNC"
                    )
                    ExpandedUniversalButton(
                        onClick = {
                            openFileDialog(FileAccessType.OPEN)
                        },
                        iconRes = R.drawable.save_as_pdf_icon,
                        text = "Сохранить файл"
                    )
                    ExpandedUniversalButton(
                        onClick = {
                            openFileDialog(FileAccessType.SAVE)
                        },
                        iconRes = R.drawable.open_file,
                        text = "Открыть файл"
                    )
                    Button(
                        onClick = {
                            changePointsVisibility()
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .width(200.dp)
                            .clip(RectangleShape),
                        contentPadding = PaddingValues(0.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (pointsVisibility) {
                                true -> Color.White
                                false -> Purple10
                            },
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = when (pointsVisibility) {
                                    true -> painterResource(id = R.drawable.view)
                                    false -> painterResource(id = R.drawable.hide)
                                },
                                contentDescription = null,
                                tint = when (pointsVisibility) {
                                    true -> Color.Black
                                    false -> Color.White
                                },
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                                    .size(25.dp)
                            )
                            Text(
                                text = "Условные обозначения",
                                modifier = Modifier.padding(5.dp),
                                color = when (pointsVisibility) {
                                    true -> Color.Black
                                    false -> Color.White
                                },
                                fontSize = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if(state.fileDialog){
        FileAccessDialog(
            onClick = {
                vm.onFileDialogChange()
            },
            groupVM = ,//TODO
            accessType = state.fileAccessType
        )
    }
}