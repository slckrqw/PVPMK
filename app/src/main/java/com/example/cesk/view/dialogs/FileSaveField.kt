package com.example.cesk.view.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.cesk.logic.file_management.saveAsCNC
import com.example.cesk.logic.file_management.saveFile
import com.example.cesk.logic.validate
import com.example.cesk.model.enums.FileAccessType
import com.example.cesk.ui.theme.Purple10
import com.example.cesk.logic.PvpFile

@Composable
fun FileSaveField(
    pvpFile: PvpFile,
    onClick: () -> Unit,
    saveType: FileAccessType
){
    var fileName by remember {
        mutableStateOf("")
    }
    var isError by remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    Row {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                text = "Сохранить файл",
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
                    if (!validate(fileName)) {
                        if(saveType == FileAccessType.SAVE) {
                            saveFile(
                                fileName,
                                pvpFile.getGroupList(),
                                context
                            )
                        }
                        else saveAsCNC(
                            fileName,
                            pvpFile.getGroupList(),
                            context
                        )
                        onClick()
                    } else {
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
                    text = "Сохранить",
                    fontSize = 17.sp,
                    color = Color.White
                )
            }
        }
    }
}