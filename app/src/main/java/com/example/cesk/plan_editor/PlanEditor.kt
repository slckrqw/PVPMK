package com.example.cesk.plan_editor

import android.Manifest
import android.graphics.Picture
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cesk.R
import com.example.cesk.draw_logic.MakePoint
import com.example.cesk.draw_logic.savePdf
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.model.enums.DialogType
import com.example.cesk.navigation.Screen
import com.example.cesk.reusable_interface.ExpandedUniversalButton
import com.example.cesk.reusable_interface.UniversalButton
import com.example.cesk.reusable_interface.dialogs.ConstructionAddDialog
import com.example.cesk.reusable_interface.dialogs.GroupDialog
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Green10
import com.example.cesk.ui.theme.Purple10
import com.example.cesk.view_models.GroupViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PlanEditor(
    groupVM: GroupViewModel = viewModel(),
    planEditorVM: PlanEditorViewModel = viewModel(),
    navController: NavController
) {

    var currentGroup by remember{
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
                modifier = canvasModifier
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
                                filePermission.launchMultiplePermissionRequest()
                                if(filePermission.allPermissionsGranted) {
                                    groupVM.getCurrentGroup()?.let {
                                        savePdf(context, picture, it)
                                    }
                                }
                                else Toast.makeText(context, "Для создания PDF требуется разрешение", Toast.LENGTH_LONG).show()
                            },
                            iconRes = R.drawable.pdf_convert
                        )
                        UniversalButton(
                            onClick = {
                                filePermission.launchMultiplePermissionRequest()
                                if(filePermission.allPermissionsGranted) {
                                    val file = File(
                                        context
                                            .getExternalFilesDir(
                                                Environment.DIRECTORY_DOCUMENTS
                                            ), "test.txt"
                                    )

                                    val fileStream = FileOutputStream(file)
                                    val outStream = ObjectOutputStream(fileStream)

                                    outStream.writeObject(groupVM.getGroupList())
                                    outStream.close()
                                    fileStream.close()

                                    Toast.makeText(
                                        context,
                                        "Файл сохранён как ${file.absolutePath}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                else Toast.makeText(context, "Для сохранения файла требуется разрешение", Toast.LENGTH_LONG).show()
                            },
                            iconRes = R.drawable.save_as_pdf_icon
                        )
                        UniversalButton(
                            onClick = {
                                filePermission.launchMultiplePermissionRequest()
                                if(filePermission.allPermissionsGranted) {
                                    val file = File(
                                        context
                                            .getExternalFilesDir(
                                                Environment.DIRECTORY_DOCUMENTS
                                            ), "test.txt"
                                    )
                                    val fileStream = FileInputStream(file)
                                    val inStream = ObjectInputStream(fileStream)

                                    val item = inStream.readObject() as MutableList<Group>

                                    groupVM.setGroupList(item)

                                    inStream.close()
                                    fileStream.close()
                                }
                                else Toast.makeText(context, "Для открытия файла требуется разрешение", Toast.LENGTH_LONG).show()
                            },
                            iconRes = R.drawable.open_file
                        )
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

                                val dir = File(
                                    Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS),"PVPMK"
                                )
                                dir.mkdir()

                                val file = File(
                                    Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS),"PVPMK/test.txt")

                                val fileStream = FileOutputStream(file)
                                val outStream = ObjectOutputStream(fileStream)

                                outStream.writeObject(groupVM.getGroupList())
                                outStream.close()
                                fileStream.close()

                                Toast.makeText(context, "Файл сохранён как ${file.absolutePath}", Toast.LENGTH_LONG).show()
                            },
                            iconRes = R.drawable.save_as_pdf_icon,
                            text = "Сохранить файл"
                        )
                        ExpandedUniversalButton(
                            onClick = {
                                val file = File(
                                    Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS),"test.txt")
                                val fileStream = FileInputStream(file)
                                val inStream = ObjectInputStream(fileStream)

                                val item = inStream.readObject() as MutableList<Group>

                                groupVM.setGroupList(item)

                                inStream.close()
                                fileStream.close()
                            },
                            iconRes = R.drawable.open_file,
                            text = "Открыть файл"
                        )
                    }
                }
            }
            if(planEditorVM.getGroupsMenu()){
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(180.dp),
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
                                        planEditorVM
                                            .setGroupsMenu(false)
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
                            ){

                                ExpandedUniversalButton(
                                    onClick = {
                                        planEditorVM
                                            .setAddGroup(true)
                                    },
                                    iconRes = R.drawable.plus_icon,
                                    text = "Добавить группу"
                                )
                                Text(
                                    text = "Группы:",
                                    fontSize = 17.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }
                        }
                        items(groupVM.getGroupList()){group ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(
                                    1.dp,
                                    if (group.index == groupVM.getIndex()) {
                                        Green10
                                    } else Color.LightGray
                                ),
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            groupVM.setIndex(group.index)
                                            currentGroup = groupVM
                                                .getGroupList()
                                                .first {
                                                    it.index == groupVM.getIndex()
                                                }
                                        },
                                    )
                                    .height(40.dp)
                                    .width(200.dp),
                                shape = RectangleShape
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            groupVM.setIndex(group.index)
                                            planEditorVM.setGroupSettings(true)
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
                                        fontSize = 15.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(end = 10.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = planEditorVM.getGroupSettings()
                                            && group.index == groupVM.getIndex(),
                                    onDismissRequest = {
                                        planEditorVM.setGroupSettings(false)
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "Редактировать"
                                            )
                                        },
                                        onClick = {
                                            planEditorVM.setGroupDialogType(DialogType.EDIT)
                                            groupVM.setIndex(group.index)
                                            planEditorVM.setAddGroup(true)
                                            planEditorVM.setGroupSettings(false)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "Удалить"
                                            )
                                        },
                                        onClick = {
                                            currentGroup = group
                                            groupVM.setIndex(group.index)
                                            groupVM.deleteGroup()
                                            planEditorVM.setGroupSettings(false)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
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