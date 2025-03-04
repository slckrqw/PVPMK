package com.example.cesk.view.dialogs

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cesk.model.enums.FileAccessType
import com.example.cesk.view.reusable_interface.RecentFilesBox
import com.example.cesk.ui.theme.CESKTheme
import com.example.cesk.logic.PvpFile

@Composable
fun FileAccessDialog(
    onClick: () -> Unit = {},
    pvpFile: PvpFile,
    accessType: FileAccessType
) {

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
            when (accessType) {
                FileAccessType.SAVE -> FileSaveField(
                    pvpFile = pvpFile,
                    onClick = onClick,
                    saveType = FileAccessType.SAVE
                )
                FileAccessType.CNC -> FileSaveField(
                    pvpFile = pvpFile,
                    onClick = onClick,
                    saveType = FileAccessType.CNC
                )
                FileAccessType.OPEN -> RecentFilesBox(
                    pvpFile = pvpFile
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileAccessDialogPreview() {
    CESKTheme {

    }
}