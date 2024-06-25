package com.example.cesk

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.cesk.model.ConstructType
import com.example.cesk.model.Point
import com.example.cesk.ui.theme.Blue10
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Orange10

@Composable
fun MakePoint(pointsList: MutableList<Point>){
    pointsList.forEach {
        if(it.x!=null && it.y!=null) {
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
}

@Composable
fun TouchOnImageExample() {

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

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
    var enduranceChange by remember{
        mutableStateOf(false)
    }

    val imageModifier = Modifier
        .background(Color.LightGray)
        .pointerInput(Unit) {
            detectTapGestures { offset: Offset ->
                offsetX = offset.x
                offsetY = offset.y
                pointsList.add(Point(x = offsetX, y = offsetY))
                enduranceChange = true
            }
        }
        .onSizeChanged { imageSize = it.toSize() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Button(
                onClick = {
                    launcher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                ),
                modifier = Modifier
                    .height(50.dp)
                    .width(100.dp),
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(2.dp, Blue10),
                contentPadding = PaddingValues(0.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.add_img_icon),
                    modifier = Modifier.size(50.dp),
                    contentDescription = null
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Gray)
        ){
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = imageModifier,
                contentScale = ContentScale.Inside
            )
            MakePoint(pointsList = pointsList)
            if(enduranceChange){
                EnduranceDialog(
                    menuClose = {enduranceChange = it}
                )
            }
        }
    }
}

@Composable
fun EnduranceDialog(point: Point = Point(), menuClose: (Boolean) -> Unit = {}){

    var typeMenuSwitch by remember{
        mutableStateOf(false)
    }
    var enduranceValue by remember{
        mutableDoubleStateOf(0.0)
    }

    Dialog(
        onDismissRequest = { menuClose(false) }
    ){
        Card(
            modifier = Modifier
                .width(300.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {typeMenuSwitch = true},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Тип конструкции",
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                    DropdownMenu(
                        expanded = typeMenuSwitch,
                        onDismissRequest = { typeMenuSwitch = false }
                    ) {
                        ContructTypeDropDownItem(
                            type = ConstructType.STANDARD,
                            point = point,
                            expanded = {typeMenuSwitch = it}
                        )
                        ContructTypeDropDownItem(
                            type = ConstructType.WALL,
                            point = point,
                            expanded = {typeMenuSwitch = it}
                        )
                        ContructTypeDropDownItem(
                            type = ConstructType.PLATE,
                            point = point,
                            expanded = {typeMenuSwitch = it}
                        )
                    }
                }
                TextField(
                    value = enduranceValue.toString(),
                    onValueChange = {
                        enduranceValue = it.toDouble()
                    },
                    placeholder = {
                        Text(
                            text = "Прочность: МПа",
                            color = Color.Gray
                        )
                    },
                    label = {
                        Text(
                            text = "Прочность в МПа"
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.endurance_icon),
                            tint = Color.Gray,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
                Button(
                    onClick = {
                        menuClose(false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Blue10),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Подтвердить",
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ContructTypeDropDownItem(type: ConstructType, point: Point, expanded: (Boolean) -> Unit){
    DropdownMenuItem(
        text = {
            Text(
                text = type.value
            )
        },
        modifier = Modifier.width(150.dp),
        onClick = {
            point.construction?.type ?: type
            expanded(false)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    CESKTheme {
        EnduranceDialog()
    }
}