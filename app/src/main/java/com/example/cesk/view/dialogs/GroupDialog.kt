package com.example.cesk.view.dialogs

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cesk.R
import com.example.cesk.model.enums.DialogType
import com.example.cesk.ui.theme.Blue10
import com.example.cesk.ui.theme.Green10
import com.example.cesk.logic.PvpFile


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupDialog(
    onClick: () -> Unit = {},
    pvpFile: PvpFile,
    dialogType: DialogType = DialogType.ADD
){
    val context = LocalContext.current

    var imageUri by remember{
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if(uri != null) {
            imageUri = uri
            Toast.makeText(context, "Изображение успешно добавлено", Toast.LENGTH_LONG).show()
        }
    }

    var groupName by remember{
        mutableStateOf(
            when(dialogType){
                DialogType.ADD -> {
                    ""
                }
                DialogType.EDIT -> {
                    pvpFile.getCurrentGroup()?.name?:""
                }
            }
        )
    }

    Dialog(
        onDismissRequest = {
            onClick()
        }
    ) {
        Card {
            Column {
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
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    modifier = Modifier
                        .height(50.dp)
                        .width(300.dp)
                        .combinedClickable(
                            onClick = {
                                launcher.launch("image/*")
                            },
                            onLongClick = {
                                pvpFile.getCurrentGroup()?.image = "null"
                                imageUri = null
                                Toast.makeText(
                                    context,
                                    "Изображение успешно удалено",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        ),
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(2.dp, Blue10)
                ) {
                    Row(
                        modifier = Modifier.width(300.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.add_img_icon),
                            modifier = Modifier.size(50.dp),
                            contentDescription = null
                        )
                    }
                }
                Button(
                    onClick = {
                        if(dialogType == DialogType.ADD){
                            pvpFile.addGroup(groupName, imageUri.toString())
                            onClick()
                        }
                        else{
                           if(imageUri != null){
                               pvpFile
                                   .editGroup(
                                       groupName,
                                       imageUri.toString()
                                   )
                           }
                           else{
                               pvpFile.getCurrentGroup()?.image?.let {
                                   pvpFile
                                       .editGroup(
                                           groupName,
                                           it
                                       )
                               }
                           }

                            onClick()
                        }
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