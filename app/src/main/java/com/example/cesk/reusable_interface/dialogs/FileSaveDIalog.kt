package com.example.cesk.reusable_interface.dialogs

import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cesk.model.Group
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.ui.theme.Purple10
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

@Composable
fun FileSaveDialog(
    onClick: () -> Unit,
    groupList: List<Group>
){

    var fileName by remember{
        mutableStateOf("")
    }
    val context = LocalContext.current
    
    Dialog(
        onDismissRequest = {
            onClick()
        }
    ) {
        Card(
            colors = CardDefaults
                .cardColors(containerColor = Color.White),
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Text(
                    text = "Сохранение файла:",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 15.dp)
                )
                TextField(
                    value = fileName,
                    onValueChange = {
                        fileName = it
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
                    singleLine = true
                )
                Button(
                    onClick = {
                        val dir = File(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS),"PVPMK"
                        )
                        dir.mkdir()

                        val file = File(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS),"PVPMK/${fileName}.txt")

                        val fileStream = FileOutputStream(file)
                        val outStream = ObjectOutputStream(fileStream)

                        outStream.writeObject(groupList)
                        outStream.close()
                        fileStream.close()

                        Toast.makeText(context, "Файл сохранён как ${file.absolutePath}", Toast.LENGTH_LONG).show()
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
}

@Preview(showBackground = true)
@Composable
fun FileDialogPreview() {
    CESKTheme {
    }
}