package com.example.cesk.logic

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.cesk.model.Group
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

fun saveAsCNC(
    groupList: List<Group>,
    context: Context
){
    var tempString = ""

    if(groupList.isEmpty()){
        Toast.makeText(
            context,
            "List of group is empty!",
            Toast.LENGTH_LONG
        ).show()
    } else {
        groupList.forEach { group ->
            tempString += if(group.name == ""){
                "no name"
            }
            else group.name
            tempString += "\n"

            tempString += if(group.image == "null"){
                "no image"
            }
            else group.image
            tempString += "\n"

            if(group.constructions.isEmpty()){
                tempString += "no constructions\n"
            }
            else {
                group.constructions.forEach { construction ->
                    tempString += "${construction.type.value}\n"

                    if (construction.tests.isEmpty()) {
                        tempString += "no tests"
                    } else {
                        construction.tests.forEach { test ->
                            tempString += "$test "
                        }
                    }
                    tempString += "\n"

                    tempString +=
                        "${construction.point.x} " +
                                "${construction.point.y}\n"

                    tempString += if (construction.note == "") {
                        "note is empty\n"
                    } else construction.note + "\n"
                }
            }

            tempString += "\n"
        }

        val dir = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS
            ), "CNC"
        )
        dir.mkdir()

        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS
            ), "CNC/${System.currentTimeMillis()}.txt"
        )

        val fileStream = FileOutputStream(file)
        val outStream = ObjectOutputStream(fileStream)

        outStream.writeObject(tempString)
        outStream.close()
        fileStream.close()

        Toast.makeText(
            context,
            "Файл сохранён как ${file.absolutePath}",
            Toast.LENGTH_LONG
        ).show()
    }
}