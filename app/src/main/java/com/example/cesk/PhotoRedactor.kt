package com.example.cesk

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Orange10

@Composable
fun MakePoint(pointsList: MutableList<Point>){
    pointsList.forEach {
        Canvas(
            modifier = Modifier
                .fillMaxSize(),
            onDraw = {
                drawCircle(
                    radius = 20f,
                    color = Orange10,
                    center = Offset(it.x, it.y)
                )
            }
        )
    }
}

@Composable
fun TouchOnImageExample() {

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(Size.Zero) }
    var makePoint by remember{
        mutableStateOf(false)
    }
    var scaledX by remember {
        mutableFloatStateOf(0f)
    }
    var scaledY by remember {
        mutableFloatStateOf(0f)
    }
    val pointsList = remember{
        mutableStateListOf(Point())
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val imageModifier = Modifier
        .background(Color.LightGray)
        .pointerInput(Unit) {
            detectTapGestures { offset: Offset ->
                offsetX = offset.x
                offsetY = offset.y
                pointsList.add(Point(x = offsetX, y = offsetY))
            }
        }
        .onSizeChanged { imageSize = it.toSize() }

    Box {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = imageModifier,
            contentScale = ContentScale.None
        )
        MakePoint(pointsList = pointsList)
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "select image")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    CESKTheme {
        TouchOnImageExample()
    }
}