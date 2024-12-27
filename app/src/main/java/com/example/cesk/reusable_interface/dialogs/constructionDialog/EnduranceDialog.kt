package com.example.cesk.reusable_interface.dialogs.constructionDialog

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cesk.R
import com.example.cesk.model.Construction
import com.example.cesk.ui.theme.CESKTheme
import kotlinx.coroutines.android.awaitFrame

@Composable
fun EnduranceDialog(
    onClick: () -> Unit = {},
    testsList: MutableList<Double>,
    construction: Construction
){
    var endurance by remember{
        mutableStateOf("")
    }

    val showKeyboard = remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(focusRequester) {
        if (showKeyboard.value) {
            focusRequester.requestFocus()
            awaitFrame()
            keyboard?.show()
        }
    }

    Dialog(
        onDismissRequest = {
            onClick()
        }
    ) {
        TextField(
            value = endurance,
            onValueChange = {endurance = it},
            leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.endurance_icon),
                        modifier = Modifier.size(30.dp),
                        contentDescription = null
                    )
            },
            placeholder = {
                Text(
                    text = "Прочность в МПа",
                    fontSize = 15.sp
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                .copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if(endurance != "") {
                       testsList
                           .add(endurance.toDouble())
                       construction.tests
                           .add(endurance.toDouble())
                    }
                    onClick()
                }
            ),
            modifier = Modifier.focusRequester(focusRequester)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EndurancePreview() {
    CESKTheme {
    }
}