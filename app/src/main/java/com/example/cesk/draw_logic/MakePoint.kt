package com.example.cesk.draw_logic

import android.content.ContentResolver
import android.graphics.ImageDecoder
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.plan_editor.PlanEditorViewModel
import com.example.cesk.ui.theme.Orange10
import com.example.cesk.view_models.GroupViewModel

@Composable
fun MakePoint(
    groupViewModel: GroupViewModel = viewModel(),
    planEditorViewModel: PlanEditorViewModel = viewModel(),
    modifier: Modifier
){

    val textMeasurer = rememberTextMeasurer()

    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    val source = groupViewModel.getCurrentGroup()?.image?.let { ImageDecoder.createSource(contentResolver, it) }
    val imageBitmap = source?.let { ImageDecoder.decodeBitmap(it).asImageBitmap() }

    Canvas(
        modifier = modifier,
        onDraw = {
            scale(planEditorViewModel.getCanvasScale()) {
                if (source != null && imageBitmap != null) {
                    drawImage(
                        imageBitmap
                    )
                }
                groupViewModel.getCurrentGroup()?.constructions?.forEach {
                    if (it.point.x != null && it.point.y != null) {
                        val textLayout = textMeasurer.measure(
                            text = it.averageEndurance.toString(),
                            style = TextStyle(fontSize = 28.sp)
                        )
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(it.point.x!!, it.point.y!!),
                            size = Size(100f, 100f)
                        )
                        drawCircle(
                            radius = 20f,
                            color = Orange10,
                            center = Offset(it.point.x!! + 10, it.point.y!! + 10)
                        )
                        drawText(
                            textLayout,
                            topLeft = Offset(it.point.x!! + 10, it.point.y!! + 25)
                        )
                    }
                }
            }
        }
    )
}