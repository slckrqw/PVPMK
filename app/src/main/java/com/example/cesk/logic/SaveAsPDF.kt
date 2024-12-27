package com.example.cesk.logic

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.Picture
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.ui.graphics.asComposePaint
import com.example.cesk.model.Group
import com.example.cesk.model.enums.ConstructionType
import java.io.File

fun savePdf(
    context: Context,
    picture: Picture,
    currentGroup: Group
) {

    val target = if(currentGroup.image == "null"){
        Bitmap.createBitmap(
            picture.width,
            picture.height,
            Bitmap.Config.ARGB_8888
        )
    }else{
        val contentResolver: ContentResolver = context.contentResolver
        val groupImage = Uri.parse(currentGroup.image)
        val source = groupImage.let { ImageDecoder.createSource(contentResolver, it) }
        val imageBitmap = source.let { ImageDecoder.decodeBitmap(it)}
        imageBitmap.copy(Bitmap.Config.ARGB_8888, false)
    }


    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(target.width, target.height, 1).create()
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
            it.point.x!! + 150f,
            it.point.y!! + 80f,
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
                ConstructionType.NOTHING -> "Н "
                ConstructionType.WALL -> "Ст "
                ConstructionType.PLATE -> "Пл "
                ConstructionType.BEAM -> "Б "
                ConstructionType.COLUMN -> "К "
                ConstructionType.FOUNDATION -> "Ф "
                ConstructionType.MONOLITHIC -> "Мб "
                ConstructionType.PYLON -> "П "
            }
                    + it.averageEndurance.toString(),
            it.point.x!!+15,
            it.point.y!!+45,
            textPaint
        )
    }
    pdfDocument.finishPage(page)

    val dir = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ), "PDF"
    )
    dir.mkdir()

    val file = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ), "PDF/${currentGroup.name}.pdf"
    )
    pdfDocument.writeTo(file.outputStream())
    pdfDocument.close()

    Toast.makeText(
        context,
        "PDF saved at ${file.absolutePath}",
        Toast.LENGTH_LONG
    ).show()
}