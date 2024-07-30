package com.example.cesk.plan_editor

import android.graphics.Picture
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.R
import com.example.cesk.draw_logic.MakePoint
import com.example.cesk.draw_logic.createBitmapFromPicture
import com.example.cesk.draw_logic.saveToDisk
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.model.enums.ClickType
import com.example.cesk.model.enums.DialogType
import com.example.cesk.reusable_interface.UniversalButton
import com.example.cesk.reusable_interface.dialogs.ConstructionAddDialog
import com.example.cesk.reusable_interface.dialogs.ConstructionDeleteDialog
import com.example.cesk.reusable_interface.dialogs.GroupDialog
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Green10
import com.example.cesk.view_models.GroupViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanEditor(
    groupVM: GroupViewModel = viewModel(),
    planEditorVM: PlanEditorViewModel = viewModel()
) {

    var currentGroup by remember{
        mutableStateOf(Group())
    }
    var currentConstruction by remember{
        mutableStateOf(Construction())
    }

    val picture = remember{ Picture() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(Size.Zero) }
    val canvasModifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { offset: Offset ->
                offsetX = offset.x
                offsetY = offset.y

                when (planEditorVM.getClickType()) {

                    ClickType.ADD -> {
                        if (currentGroup.constructions.isEmpty()) {
                            val newConstruction = Construction()

                            newConstruction.point.x = offsetX
                            newConstruction.point.y = offsetY
                            currentGroup.constructions
                                .add(newConstruction)

                            currentConstruction = newConstruction
                            planEditorVM.setConstructionDialogType(DialogType.ADD)
                            planEditorVM.setAddConstruction(true)
                        } else {
                            if (currentGroup.constructions
                                    .none {
                                        (it.point.x!! <= offsetX + 50 && it.point.x!! >= offsetX - 50)
                                                && (it.point.y!! <= offsetY + 50 && it.point.y!! >= offsetY - 50)
                                    }
                            ) {
                                val newConstruction = Construction()

                                newConstruction.point.x = offsetX
                                newConstruction.point.y = offsetY
                                currentGroup.constructions
                                    .add(newConstruction)

                                currentConstruction = newConstruction
                                planEditorVM.setConstructionDialogType(DialogType.ADD)
                                planEditorVM.setAddConstruction(true)
                            } else {
                                currentConstruction = currentGroup.constructions.first {
                                    (it.point.x!! <= offsetX + 50 && it.point.x!! >= offsetX - 50)
                                            && (it.point.y!! <= offsetY + 50 && it.point.y!! >= offsetY - 50)
                                }
                                planEditorVM.setConstructionDialogType(DialogType.EDIT)
                                planEditorVM.setAddConstruction(true)
                            }
                        }
                    }

                    ClickType.DELETE -> {
                        if (
                            currentGroup.constructions.isNotEmpty() &&
                            !currentGroup.constructions.none {
                                (it.point.x!! <= offsetX + 50 && it.point.x!! >= offsetX - 50)
                                        && (it.point.y!! <= offsetY + 50 && it.point.y!! >= offsetY - 50)
                            }
                        ) {
                            currentConstruction = currentGroup.constructions.first {
                                (it.point.x!! <= offsetX + 50 && it.point.x!! >= offsetX - 50)
                                        && (it.point.y!! <= offsetY + 50 && it.point.y!! >= offsetY - 50)
                            }
                            planEditorVM.setAddConstruction(true)
                        }
                    }
                }
            }
        }
        .onSizeChanged { imageSize = it.toSize() }
        .drawWithCache {
            val width = this.size.width.toInt()
            val height = this.size.height.toInt()
            onDrawWithContent {
                val pictureCanvas =
                    androidx.compose.ui.graphics.Canvas(
                        picture.beginRecording(
                            width,
                            height
                        )
                    )
                draw(this, this.layoutDirection, pictureCanvas, this.size) {
                    this@onDrawWithContent.drawContent()
                }
                picture.endRecording()

                drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
            }
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        if(groupVM.getGroupList().isNotEmpty()) {
            MakePoint(
                modifier = canvasModifier
            )
        }
        when (planEditorVM.getTools()) {
            true -> Card(
                modifier = Modifier
                    .fillMaxHeight(),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column (
                    modifier = Modifier.fillMaxHeight()
                ){
                    UniversalButton(
                        onClick = {
                            planEditorVM.setTools(false)
                        },
                        iconRes = R.drawable.roll_up_icon
                    )
                    UniversalButton(
                        onClick = {
                            planEditorVM.setAddGroup(true)
                        },
                        iconRes = R.drawable.plus_icon
                    )
                    UniversalButton(
                        onClick = {
                            planEditorVM.setClickType(ClickType.ADD)
                        },
                        iconRes = R.drawable.choose_icon,
                        isActive = planEditorVM.getClickType() == ClickType.ADD
                    )
                    UniversalButton(
                        onClick = {
                            planEditorVM.setClickType(ClickType.DELETE)
                        },
                        iconRes = R.drawable.delete_icon,
                        isActive = planEditorVM.getClickType() == ClickType.DELETE
                    )
                    UniversalButton(
                        onClick = {
                            val pdfBitmap = createBitmapFromPicture(picture)
                            coroutineScope.launch {
                                pdfBitmap.saveToDisk(context)
                            }
                            //Toast.makeText(context, "Неверное разрешение экрана", Toast.LENGTH_SHORT).show()
                        },
                        iconRes = R.drawable.save_as_pdf_icon
                    )
                    UniversalButton(
                        onClick = {
                            planEditorVM.increaseCanvasScale()
                        },
                        iconRes = R.drawable.plus_icon
                    )
                    UniversalButton(
                        onClick = {
                            planEditorVM.decreaseCanvasScale()
                        },
                        iconRes = R.drawable.minus_icon
                    )
                    groupVM.getGroupList().forEach { group ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(
                                1.dp,
                                if(group.index == groupVM.getIndex()){
                                    Green10
                                }
                                else Color.LightGray
                            ),
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        groupVM.setIndex(group.index)
                                        currentGroup = groupVM
                                            .getGroupList()
                                            .first {
                                                it.index == groupVM.getIndex()
                                            }
                                    },
                                    onDoubleClick = {
                                        planEditorVM.setGroupDialogType(DialogType.EDIT)
                                        groupVM.setIndex(group.index)
                                        planEditorVM.setAddGroup(true)
                                    },
                                    onLongClick = {
                                        currentGroup = group
                                        groupVM.setIndex(group.index)
                                        groupVM.deleteGroup()
                                    }
                                )
                                .height(40.dp)
                                .width(200.dp),
                            shape = RectangleShape
                        ){
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = group.name,
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
            false -> UniversalButton(
                onClick = { planEditorVM.setTools(true) },
                iconRes = R.drawable.expand_icon
            )
        }
    }
    if (planEditorVM.getAddConstruction()) {
        when (planEditorVM.getClickType()) {
            ClickType.ADD -> {
                ConstructionAddDialog(
                    onCLick = { planEditorVM.setAddConstruction(false) },
                    construction = currentConstruction,
                    dialogType = planEditorVM.getConstructionDialogType(),
                    group = currentGroup
                )
            }

            ClickType.DELETE -> {
                ConstructionDeleteDialog(
                    group = currentGroup,
                    construction = currentConstruction,
                    onClick = { planEditorVM.setAddConstruction(false) }
                )
            }
        }
    }
    if(planEditorVM.getAddGroup()){
        when(planEditorVM.getGroupDialogType()){
            DialogType.ADD -> GroupDialog(
                onClick = {planEditorVM.setAddGroup(false)},
                dialogType = DialogType.ADD
            )
            DialogType.EDIT -> GroupDialog(
                onClick = { planEditorVM.setAddGroup(false) },
                dialogType = DialogType.EDIT
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    CESKTheme {
    }
}