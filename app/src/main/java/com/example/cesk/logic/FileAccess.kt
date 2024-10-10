package com.example.cesk.logic

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.cesk.model.Group
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun openFile(
    fileName: String,
    context: Context
):MutableList<Group>?{

    val dir = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ), "PVP"
    )
    dir.mkdir()

    try {
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS
            ), "PVP/${fileName}.pvp"
        )

        val fileStream = FileInputStream(file)
        val inStream = ObjectInputStream(fileStream)

        val item = inStream.readObject() as MutableList<Group>

        inStream.close()
        fileStream.close()

        return item
    }
    catch (e: Exception){
        Toast.makeText(
            context,
            "Не существует файла с таким имененем!",
            Toast.LENGTH_LONG
        ).show()
        return null
    }
}

fun saveFile(
    fileName: String,
    groupList: List<Group>,
    context: Context
){
    val dir = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ), "PVP"
    )
    dir.mkdir()

    val file = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ), "PVP/${fileName}.pvp"
    )

    val fileStream = FileOutputStream(file)
    val outStream = ObjectOutputStream(fileStream)

    outStream.writeObject(groupList)
    outStream.close()
    fileStream.close()


}