package com.example.cesk

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.example.cesk.model.Construction
import com.example.cesk.model.Group
import com.example.cesk.model.Point
import com.example.cesk.reusable_interface.UniversalButton
import com.example.cesk.ui.theme.Blue10
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Green10
import com.example.cesk.ui.theme.Orange10
import kotlin.math.roundToInt

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TouchOnImageExample() {

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    val pointsList = remember{
        mutableStateListOf<Point>()
    }
    var enduranceChange by remember{
        mutableStateOf(false)
    }
    var expandedMenu by remember{
        mutableStateOf(false)
    }
    var groupsList = remember{
        mutableStateListOf<Group>()
    }
    var addGroupMenu by remember{
        mutableStateOf(false)
    }
    var currentGroup by remember{
        mutableStateOf(Group())
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
   /* Row(
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
    */

    */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color.Gray)
    ) {
        if(!groupsList.isEmpty()) {
            currentGroup.image?.let {
                AsyncImage(
                    model = currentGroup.image,
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Inside
                )
            }
        }
        when (expandedMenu) {
            true -> Card(
                modifier = Modifier
                    .fillMaxHeight(),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column (
                    modifier = Modifier.fillMaxHeight()
                ){
                    Row(

                    ) {
                        UniversalButton(
                            onClick = {expandedMenu = false},
                            iconRes = R.drawable.roll_up_icon
                        )
                        UniversalButton(
                            onClick = {
                                addGroupMenu = true
                            },
                            iconRes = R.drawable.plus_icon
                        )
                    }
                    groupsList.forEach {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(
                                1.dp,
                                if(currentGroup == it){
                                    Green10
                                }
                                else Color.LightGray
                            ),
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        currentGroup = it
                                    },
                                    onLongClick = {
                                        currentGroup = it
                                        groupsList.remove(currentGroup)
                                    }
                                )
                                .padding(end = 5.dp)
                                .height(40.dp)
                                .width(200.dp),
                            shape = RectangleShape
                        ){
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it.name,
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
            false -> UniversalButton(
                onClick = { expandedMenu = true },
                iconRes = R.drawable.expand_icon
            )
        }
    }
    MakePoint(pointsList = pointsList)
    if (enduranceChange) {
        EnduranceDialog(
            menuClose = { enduranceChange = it }
        )
    }
    if(addGroupMenu){
        GroupDialog(
            expanded = {addGroupMenu = it},
            groupsList = groupsList
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EnduranceDialog(point: Point = Point(), menuClose: (Boolean) -> Unit = {}){

    var typeMenuSwitch by remember{
        mutableStateOf(false)
    }
    var enduranceValue by remember{
        mutableDoubleStateOf(0.0)
    }
    var newConstruction by remember{
        mutableStateOf(Construction())
    }
    var averageEndurance by remember{
        mutableDoubleStateOf(0.0)
    }

    averageEndurance = (newConstruction.tests.sum()/newConstruction.tests.size)
    Dialog(
        onDismissRequest = {menuClose(false)}
    ) {
        Card(
            modifier = Modifier
                .width(515.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .width(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    newConstruction.tests.forEach {
                        Text(
                            text = it.toString(),
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        newConstruction.tests.remove(it)
                                    }
                                )
                        )
                    }
                    Text(
                        text = "Ср: " + averageEndurance.roundToInt().toString(),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
                        .width(400.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { typeMenuSwitch = true },
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
                                expanded = { typeMenuSwitch = it }
                            )
                            ContructTypeDropDownItem(
                                type = ConstructType.WALL,
                                point = point,
                                expanded = { typeMenuSwitch = it }
                            )
                            ContructTypeDropDownItem(
                                type = ConstructType.PLATE,
                                point = point,
                                expanded = { typeMenuSwitch = it }
                            )
                        }
                    }
                    /*TextField(
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
                    )*/
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
}

@Composable
fun GroupDialog(expanded: (Boolean) -> Unit = {}, groupsList: MutableList<Group> = mutableListOf()){
    var imageUri by remember{
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val newGroup by remember{
        mutableStateOf(Group())
    }
    var groupName by remember{
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = {expanded(false)}
    ) {
        Card(

        ) {
            Column(

            ) {
                TextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    modifier = Modifier.width(300.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = {
                        Text(
                            text = "Название"
                        )
                    },
                    singleLine = true
                )
                Button(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                    ),
                    modifier = Modifier
                        .height(50.dp)
                        .width(300.dp),
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(2.dp, Blue10),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.add_img_icon),
                        modifier = Modifier.size(50.dp),
                        contentDescription = null
                    )
                }
                Button(
                    onClick = {
                        newGroup.name = groupName
                        newGroup.image = imageUri
                        groupsList.add(newGroup)
                        expanded(false)
                    },
                    modifier = Modifier.width(300.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green10)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupCard(){
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            1.dp, Color.LightGray
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = {

                },
                onLongClick = {

                }
            )
            .height(40.dp)
            .width(200.dp),
        shape = RectangleShape
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Конструкции 1-го этажа",
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    CESKTheme {
        EnduranceDialog()
    }
}