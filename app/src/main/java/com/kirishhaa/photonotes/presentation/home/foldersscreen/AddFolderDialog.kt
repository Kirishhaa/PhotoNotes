package com.kirishhaa.photonotes.presentation.home.foldersscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun AddFolderDialog(
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit
) {

    var folderException by remember {
        mutableStateOf(false)
    }

    var folderNameValue by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(180.dp)
                .background(
                    color = colorResource(R.color.secondary_container),
                    shape = RoundedCornerShape(24.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.add_new_folder),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.comic)),
                color = colorResource(R.color.primary_color)
            )
            Spacer(Modifier.height(20.dp))
            TextField(
                value = folderNameValue,
                onValueChange = { folderNameValue = it },
                modifier = Modifier.width(250.dp),
                isError = folderException,
                label = {
                    Text(
                        text = stringResource(R.string.folder_name),
                        fontFamily = FontFamily(Font(R.font.comic)),
                        color = colorResource(R.color.primary_color)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colorResource(R.color.primary_color),
                    cursorColor = colorResource(R.color.primary_color)
                )
            )
        }
        Row(
            modifier = Modifier
                .width(300.dp)
                .offset(y = 100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(130.dp)
                    .height(46.dp)
                    .pulsateClick(clickable = true, onClick = { onDismiss() })
                    .background(
                        color = colorResource(R.color.on_secondary_container),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    fontFamily = FontFamily(Font(R.font.comic)),
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(130.dp)
                    .height(46.dp)
                    .pulsateClick(clickable = true, onClick = {
                        if (folderNameValue.trim().isEmpty()) {
                            folderException = true
                        } else {
                            onAdd(folderNameValue)
                        }
                    }
                    )
                    .background(
                        color = colorResource(R.color.on_secondary_container),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Text(
                    text = context.getString(R.string.add),
                    fontFamily = FontFamily(Font(R.font.comic)),
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
    }
}