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
import com.example.cesk.model.enums.ConstructionType
import com.example.cesk.ui.theme.Orange10
import java.io.IOException

@Composable
fun MakePoint(
    pvpFile: PvpFile,
    pointsVisibility: Boolean,
    modifier: Modifier,
    canvasScale: Float
){
    val textMeasurer = rememberTextMeasurer()

    val context = LocalContext.current 
    val contentResolver: ContentResolver = context.contentResolver

    val groupImage: Uri? = if(pvpFile.getCurrentGroup()?.image == "null"){
        null
    }else Uri.parse(pvpFile.getCurrentGroup()?.image)

    var imageBitmap: ImageBitmap? = null
    try {
        val source = groupImage?.let { ImageDecoder.createSource(contentResolver, it) }
        imageBitmap = source?.let { ImageDecoder.decodeBitmap(it).asImageBitmap() }
    }catch (_: IOException){

    }


    Canvas(
        modifier = modifier,
        onDraw = {
            scale(canvasScale) {
                if (imageBitmap != null) {
                    drawImage(
                        imageBitmap
                    )
                }
                pvpFile.getCurrentGroup()?.constructions?.forEach {
                    val textLayout = textMeasurer.measure(
                        text =
                        when(it.type) {
                            ConstructionType.NOTHING -> "Н "
                            ConstructionType.WALL -> "Ст "
                            ConstructionType.PLATE -> "Пл "
                            ConstructionType.BEAM -> "Б "
                            ConstructionType.COLUMN -> "К "
                            ConstructionType.FOUNDATION -> "Ф "
                            ConstructionType.MONOLITHIC -> "Мб "
                            ConstructionType.PYLON -> "П "
                        } + it.averageEndurance.toString(),
                        style = TextStyle(fontSize = 28.sp)
                    )
                    if(pointsVisibility) {
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