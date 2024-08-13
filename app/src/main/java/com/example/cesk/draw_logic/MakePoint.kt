package com.example.cesk.draw_logic

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.Picture
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.model.Group
import com.example.cesk.model.enums.ConstructType
import com.example.cesk.plan_editor.PlanEditorViewModel
import com.example.cesk.ui.theme.Orange10
import com.example.cesk.view_models.GroupViewModel
import java.io.File

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
                    val textLayout = textMeasurer.measure(
                        text =
                        when(it.type) {
                            ConstructType.NOTHING -> "Н "
                            ConstructType.WALL -> "Ст "
                            ConstructType.PLATE -> "Пл "
                        } + it.averageEndurance.toString(),
                        style = TextStyle(fontSize = 28.sp)
                    )
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(it.point.x!!, it.point.y!!),
                        size = Size(200f, 200f)
                    )
                    drawCircle(
                        radius = 20f,
                        color = Orange10,
                        center = Offset(it.point.x!! + 10, it.point.y!! + 10)
                    )
                    drawText(
                        textLayout,
                        topLeft = Offset(it.point.x!! + 15, it.point.y!! + 45)
                    )
                }
            }
        }
    )
}

fun savePdf(
    context: Context,
    picture: Picture,
    currentGroup: Group
) {

    val contentResolver: ContentResolver = context.contentResolver
    val source = currentGroup.image?.let { ImageDecoder.createSource(contentResolver, it) }
    val imageBitmap = source?.let { ImageDecoder.decodeBitmap(it)}
    val target = imageBitmap?.copy(Bitmap.Config.ARGB_8888, false)

    val bitmap = Bitmap.createBitmap(
        picture.width,
        picture.height,
        Bitmap.Config.ARGB_8888
    )

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    if (target != null) {
        canvas.drawBitmap(target, 0f, 0f, null)
    }

    val pointPaint = Paint()
    pointPaint.asComposePaint()
    pointPaint.isAntiAlias = true
    pointPaint.style = Paint.Style.FILL_AND_STROKE
    pointPaint.setARGB(255,255,127,39)

    val textPaint = Paint()
    textPaint.textSize = 45f
    textPaint.setARGB(255, 0, 0, 0)

    val rectPaint = Paint()
    rectPaint.setARGB(255,255,255,255)

    currentGroup.constructions.forEach {
        canvas.drawRect(
            it.point.x!!,
            it.point.y!!,
            it.point.x!! + 200f,
            it.point.y!! + 200f,
            rectPaint
        )
        canvas.drawCircle(
            it.point.x!!,
            it.point.y!!,
            20f,
            pointPaint
        )
        canvas.drawText(
            when(it.type) {
                    ConstructType.NOTHING -> "Н "
                    ConstructType.WALL -> "Ст "
                    ConstructType.PLATE -> "Пл "
                }
                + it.averageEndurance.toString(),
                it.point.x!!+15,
                it.point.y!!+45,
                textPaint
                    )
    }
    pdfDocument.finishPage(page)

    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "screenshot-${System.currentTimeMillis()}.pdf")
    pdfDocument.writeTo(file.outputStream())
    pdfDocument.close()

    Toast.makeText(context, "PDF saved at ${file.absolutePath}", Toast.LENGTH_LONG).show()
}