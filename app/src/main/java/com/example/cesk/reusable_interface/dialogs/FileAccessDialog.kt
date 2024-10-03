package com.example.cesk.reusable_interface.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.logic.openFile
import com.example.cesk.logic.saveFile
import com.example.cesk.logic.validate
import com.example.cesk.model.enums.FileAccessType
import com.example.cesk.ui.theme.Purple10
import com.example.cesk.view_models.GroupViewModel

@Composable
fun FileAccessDialog(
    onClick: () -> Unit,
    groupVM: GroupViewModel = viewModel(),
    accessType: FileAccessType
){
    var fileName by remember{
        mutableStateOf("")
    }
    var isError by remember{
        mutableStateOf(true)
    }
    val context = LocalContext.current

    val preText = when(accessType){
        FileAccessType.OPEN -> "Открыть"
        FileAccessType.SAVE -> "Сохранить"
    }

    Dialog(
        onDismissRequest = {
            onClick()
        }
    ){
        Card(
            colors = CardDefaults
                .cardColors(containerColor = Color.White),
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Text(
                    text = "$preText файл",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 15.dp)
                )
                TextField(
                    value = fileName,
                    onValueChange = {
                        fileName = it
                        isError = validate(fileName)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = {
                        Text(
                            text = "Имя файла"
                        )
                    },
                    singleLine = true,
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Лимит: 1..20",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )
                Button(
                    onClick = {
                        if(!validate(fileName)) {
                            when(accessType){

                                FileAccessType.OPEN -> groupVM
                                    .setGroupList(
                                        openFile(
                                            fileName,
                                            context
                                        )
                                    )

                                FileAccessType.SAVE -> saveFile(
                                    fileName,
                                    groupVM.getGroupList(),
                                    context
                                )

                            }
                            onClick()
                        }
                        else{
                            Toast.makeText(
                                context,
                                "Лимит длины имени: 1..20",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    colors = ButtonDefaults
                        .buttonColors(containerColor = Purple10),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = preText,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}