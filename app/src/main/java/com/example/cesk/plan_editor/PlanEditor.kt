package com.example.cesk.plan_editor

import android.Manifest
import android.graphics.Picture
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.R
import com.example.cesk.logic.MakePoint
import com.example.cesk.logic.savePdf
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.model.enums.DialogType
import com.example.cesk.model.enums.FileAccessType
import com.example.cesk.reusable_interface.ExpandedUniversalButton
import com.example.cesk.reusable_interface.UniversalButton
import com.example.cesk.reusable_interface.dialogs.FileAccessDialog
import com.example.cesk.reusable_interface.dialogs.GroupDialog
import com.example.cesk.reusable_interface.dialogs.constructionDialog.ConstructionAddDialog
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Purple10
import com.example.cesk.view_models.GroupViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PlanEditor(
    groupVM: GroupViewModel = viewModel(),
    planEditorVM: PlanEditorViewModel = viewModel(),
) {

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

    val filePermission = rememberMultiplePermissionsState(
         listOf(
             Manifest.permission.WRITE_EXTERNAL_STORAGE,
             Manifest.permission.READ_EXTERNAL_STORAGE
         )
    )


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
            when (planEditorVM.getTools()) {
                false -> Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(50.dp),
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                                .background(Purple10)
                        ) {
                            UniversalButton(
                                onClick = {
                                    planEditorVM.setTools(true)
                                },
                                iconRes = R.drawable.right_arrow_icon,
                                containerColor = Purple10
                            )
                        }
                        UniversalButton(
                            onClick = {
                                planEditorVM
                                    .setGroupsMenu(!planEditorVM.getGroupsMenu())
                            },
                            iconRes = R.drawable.menu
                        )
                        UniversalButton(
                            onClick = {
                                if(filePermission.allPermissionsGranted) {
                                    groupVM.getCurrentGroup()?.let {
                                        savePdf(context, picture, it)
                                    }
                                }
                                else {
                                    Toast.makeText(
                                        context,
                                        "Для создания PDF требуется разрешение",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    filePermission
                                        .launchMultiplePermissionRequest()
                                }
                            },
                            iconRes = R.drawable.pdf_convert
                        )
                        UniversalButton(
                            onClick = {
                                if(filePermission.allPermissionsGranted) {
                                    planEditorVM.setFileAccess(FileAccessType.CNC)
                                    planEditorVM.setFileAccessDialog(true)
                                }
                                else{
                                    Toast.makeText(
                                        context,
                                        "Для создания CNC требуется разрешение",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    filePermission
                                        .launchMultiplePermissionRequest()
                                }
                            },
                            iconRes = R.drawable.cnc_convert
                        )
                        UniversalButton(
                            onClick = {
                                if(filePermission.allPermissionsGranted) {
                                    planEditorVM.setFileAccess(FileAccessType.SAVE)
                                    planEditorVM.setFileAccessDialog(true)
                                }
                                else {
                                    Toast.makeText(
                                        context,
                                        "Для сохранения файла требуется разрешение",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    filePermission
                                        .launchMultiplePermissionRequest()
                                }
                            },
                            iconRes = R.drawable.save_as_pdf_icon
                        )
                        UniversalButton(
                            onClick = {
                                if(filePermission.allPermissionsGranted) {
                                    planEditorVM.setFileAccess(FileAccessType.OPEN)
                                    planEditorVM.setFileAccessDialog(true)
                                }
                                else {
                                    Toast.makeText(
                                        context,
                                        "Для открытия файла требуется разрешение",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    filePermission
                                         .launchMultiplePermissionRequest()
                                }
                            },
                            iconRes = R.drawable.open_file
                        )
                        Button(
                            onClick = {
                                planEditorVM
                                    .setPointsVisibility(
                                        !planEditorVM
                                            .getPointsVisibility()
                                    )
                            },
                            modifier = Modifier
                                .clip(RectangleShape)
                                .size(50.dp),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            contentPadding = PaddingValues(0.dp)
                        ){
                            Icon(
                                painter = when(planEditorVM.getPointsVisibility()){
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
                }

                true -> Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(180.dp),
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                                .background(Purple10)
                        ) {
                            ExpandedUniversalButton(
                                onClick = { planEditorVM.setTools(false) },
                                iconRes = R.drawable.left_arrow,
                                text = "Свернуть",
                                containerColor = Purple10
                            )
                        }
                        ExpandedUniversalButton(
                            onClick = {
                                planEditorVM
                                    .setGroupsMenu(!planEditorVM.getGroupsMenu())
                            },
                            iconRes = R.drawable.menu,
                            text = "Список групп"
                        )
                        ExpandedUniversalButton(
                            onClick = {
                                groupVM.getCurrentGroup()?.let {
                                    savePdf(context, picture, it)
                                }
                            },
                            iconRes = R.drawable.pdf_convert,
                            text = "Экспорт в PDF"
                        )
                        ExpandedUniversalButton(
                            onClick = {
                                if(filePermission.allPermissionsGranted) {
                                    planEditorVM.setFileAccess(FileAccessType.CNC)
                                    planEditorVM.setFileAccessDialog(true)
                                }
                                else{
                                    Toast.makeText(
                                        context,
                                        "Для создания CNC требуется разрешение",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    filePermission
                                        .launchMultiplePermissionRequest()
                                }
                            },
                            iconRes = R.drawable.cnc_convert,
                            text = "Экспорт в CNC"
                        )
                        ExpandedUniversalButton(
                            onClick = {
                                if(filePermission.allPermissionsGranted) {
                                    planEditorVM.setFileAccess(FileAccessType.SAVE)
                                    planEditorVM.setFileAccessDialog(true)
                                }
                                else {
                                    Toast.makeText(
                                        context,
                                        "Для сохранения файла требуется разрешение",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    filePermission
                                        .launchMultiplePermissionRequest()
                                }
                            },
                            iconRes = R.drawable.save_as_pdf_icon,
                            text = "Сохранить файл"
                        )
                        ExpandedUniversalButton(
                            onClick = {
                                if(filePermission.allPermissionsGranted) {
                                    planEditorVM.setFileAccess(FileAccessType.OPEN)
                                    planEditorVM.setFileAccessDialog(true)
                                }
                                else {
                                    Toast.makeText(
                                        context,
                                        "Для открытия файла требуется разрешение",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    filePermission
                                        .launchMultiplePermissionRequest()
                                }
                            },
                            iconRes = R.drawable.open_file,
                            text = "Открыть файл"
                        )
                        Button(
                            onClick = {
                                planEditorVM
                                    .setPointsVisibility(
                                        !planEditorVM
                                            .getPointsVisibility()
                                    )
                            },
                            modifier = Modifier
                                .height(50.dp)
                                .width(200.dp)
                                .clip(RectangleShape),
                            contentPadding = PaddingValues(0.dp),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when(planEditorVM.getPointsVisibility()){
                                    true -> Color.White
                                    false -> Purple10
                                },
                            )
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Icon(
                                    painter = when(planEditorVM.getPointsVisibility()){
                                        true -> painterResource(id = R.drawable.view)
                                        false -> painterResource(id = R.drawable.hide)
                                    },
                                    contentDescription = null,
                                    tint = when(planEditorVM.getPointsVisibility()){
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
                                    color = when(planEditorVM.getPointsVisibility()){
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
            /*if(planEditorVM.getGroupsMenu()){

            }*/
            //TODO
        }
    }

    if (planEditorVM.getAddConstruction()) {
        ConstructionAddDialog(
            onCLick = { planEditorVM.setAddConstruction(false) },
            construction = currentConstruction,
            dialogType = planEditorVM.getConstructionDialogType(),
            group = currentGroup
        )
    }
    if(planEditorVM.getAddGroup()){
        when(planEditorVM.getGroupDialogType()){
            DialogType.ADD -> GroupDialog(
                onClick = {planEditorVM.setAddGroup(false)},
                dialogType = DialogType.ADD,
                groupViewModel = groupVM
            )
            DialogType.EDIT -> GroupDialog(
                onClick = { planEditorVM.setAddGroup(false) },
                dialogType = DialogType.EDIT,
                groupViewModel = groupVM
            )
        }
    }
    if(planEditorVM.getFileAccessDialog()){
       FileAccessDialog(
           onClick = {
               planEditorVM
                   .setFileAccessDialog(false)
           },
           accessType = planEditorVM.getFileAccess(),
           groupVM = groupVM
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