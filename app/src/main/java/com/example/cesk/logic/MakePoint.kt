package com.example.cesk.logic

import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.model.enums.ConstructType
import com.example.cesk.plan_editor.PlanEditorViewModel
import com.example.cesk.ui.theme.Orange10
import com.example.cesk.view_models.GroupViewModel
import java.io.IOException

@Composable
fun MakePoint(
    groupViewModel: GroupViewModel = viewModel(),
    planEditorViewModel: PlanEditorViewModel = viewModel(),
    modifier: Modifier
){
    val textMeasurer = rememberTextMeasurer()

    val context = LocalContext.current 
    val contentResolver: ContentResolver = context.contentResolver

    val groupImage: Uri? = if(groupViewModel.getCurrentGroup()?.image == "null"){
        null
    }else Uri.parse(groupViewModel.getCurrentGroup()?.image)

    var imageBitmap: ImageBitmap? = null
    try {
        val source = groupImage?.let { ImageDecoder.createSource(contentResolver, it) }
        imageBitmap = source?.let { ImageDecoder.decodeBitmap(it).asImageBitmap() }
    }catch (_: IOException){

    }


    Canvas(
        modifier = modifier,
        onDraw = {
            scale(planEditorViewModel.getCanvasScale()) {
                if (imageBitmap != null) {
                    drawImage(
                        imageBitmap
                    )
                }
                groupViewModel.getCurrentGroup()?.constructions?.forEach {
                    val textLayout = textMeasurer.measure(
                        text =
                        when(it.type) {
                            ConstructType.NOTHING -> "Н "
                            ConstructType.WALL -> "Ст "
                            ConstructType.PLATE -> "Пл "
                            ConstructType.BEAM -> "Б "
                            ConstructType.COLUMN -> "К "
                            ConstructType.FOUNDATION -> "Ф "
                            ConstructType.MONOLITHIC -> "Мб "
                            ConstructType.PYLON -> "П "
                        } + it.averageEndurance.toString(),
                        style = TextStyle(fontSize = 28.sp)
                    )
                    if(planEditorViewModel.getPointsVisibility()) {
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(it.point.x!!, it.point.y!!),
                            size = Size(200f, 100f)
                        )
                        drawText(
                            textLayout,
                            topLeft = Offset(it.point.x!! + 15, it.point.y!! + 35)
                        )
                    }
                    drawCircle(
                        radius = 20f,
                        color = Orange10,
                        center = Offset(it.point.x!! + 10, it.point.y!! + 10)
                    )
                }
            }
        }
    )
}