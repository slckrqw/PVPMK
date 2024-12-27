package com.example.cesk.view.reusable_interface

import android.os.Environment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cesk.logic.openFile
import com.example.cesk.ui.theme.Purple10
import com.example.cesk.view_models.GroupViewModel
import java.io.File

@Composable
fun RecentFilesBox(
    onClick: () -> Unit = {},
    groupViewModel: GroupViewModel = viewModel()
){

    val context = LocalContext.current
    val dir = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ), "PVP"
    )
    dir.mkdir()

    val recentFiles = dir.listFiles()

    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Недавние файлы:",
                    color = Color.Black,
                    fontSize = 25.sp
                )
            }
            if (recentFiles != null) {
                items(recentFiles.size) {file ->
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(vertical = 15.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .clickable(
                                onClick = {
                                    openFile(
                                        recentFiles[file].nameWithoutExtension,
                                        context
                                    )?.let { groupList ->
                                        groupViewModel.setGroupList(
                                            groupList
                                        )
                                    }
                                    onClick()
                                }
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(15.dp),
                        border = BorderStroke(1.dp, Purple10)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = recentFiles[file].nameWithoutExtension,
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier
                                    .padding(10.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
            else{
                item{
                    Text(
                        text = "файлы отсутствуют...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}