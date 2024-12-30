package com.example.cesk.view.plan_editor

import android.graphics.Picture
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.logic.MakePoint
import com.example.cesk.logic.savePdf
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.model.enums.DialogType
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.view.dialogs.construction_dialogs.ConstructionAddDialog
import com.example.cesk.view.groups_card.GroupsCard
import com.example.cesk.view.tools_card.ToolsCard
import com.example.cesk.view_models.GroupViewModel

@Composable
fun PlanEditor(
    groupVM: GroupViewModel = viewModel(),
    vm: PlanEditorViewModel = viewModel(),
) {
    val state by vm.planEditorState.collectAsState()

    val currentGroup by remember{
        mutableStateOf(Group())
    }
    var currentConstruction by remember{
        mutableStateOf(Construction())
    }

    val picture = remember{
        Picture()
    }

    val context = LocalContext.current

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(Size.Zero) }
    val canvasModifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { offset: Offset ->
                offsetX = offset.x
                offsetY = offset.y

                if (currentGroup.constructions.isEmpty()) {
                    val newConstruction = Construction()

                    newConstruction.point.x = offsetX
                    newConstruction.point.y = offsetY
                    currentGroup.constructions
                        .add(newConstruction)

                    currentConstruction = newConstruction
                    vm.setConstructionDialogType(DialogType.ADD)
                    vm.onConstructionDialogChange()
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
                        vm.setConstructionDialogType(DialogType.ADD)
                        vm.onConstructionDialogChange()
                    } else {
                        currentConstruction = currentGroup.constructions.first {
                            (it.point.x!! <= offsetX + 50 && it.point.x!! >= offsetX - 50)
                                    && (it.point.y!! <= offsetY + 50 && it.point.y!! >= offsetY - 50)
                        }
                        vm.setConstructionDialogType(DialogType.EDIT)
                        vm.onConstructionDialogChange()
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
                    Canvas(
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
            .background(Color.Gray)
    ) {
        if(groupVM.getIndex() != 0) {
            MakePoint(
                modifier = canvasModifier,
                groupViewModel = groupVM
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ToolsCard(
                openGroupsCard = {
                    vm.onGroupsCardViewChange()
                },
                savePDF = {
                    groupVM.getCurrentGroup()?.let {
                        savePdf(context, picture, it)
                    }
                },
                changePointsVisibility = {
                    vm.onPointsVisibilityChange()
                },
                pointsVisibility = state.pointsVisibility
            )
            if(state.groupsCardView){
                GroupsCard(
                    onClick = {
                        vm.onGroupsCardViewChange()
                    },
                    groupList = groupVM.getGroupList(),
                    index = groupVM.getIndex(),
                    setIndex = {
                        groupVM.setIndex(it)
                    },
                    deleteGroup = {
                        groupVM.deleteGroup()
                    },
                )
            }
        }
    }

    if (state.constructionDialog) {
        ConstructionAddDialog(
            onCLick = { vm.onConstructionDialogChange() },
            construction = currentConstruction,
            dialogType = state.constructionDialogType,
            group = currentGroup
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    CESKTheme {
        PlanEditor()
    }
}