package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun AddTagDialog(
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit
) {

    var tagValue by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.3f)
            .background(Color.Black)
    )
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = colorResource(R.color.secondary_container))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.tag_name),
                fontSize = 28.sp,
                color = colorResource(R.color.on_surface),
                fontFamily = FontFamily(Font(R.font.comic))
            )
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = tagValue,
                onValueChange = { if (it.contains("\n").not() && it.trim().isNotEmpty()) tagValue = it },
                singleLine = true,
                modifier = Modifier.width(250.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colorResource(R.color.primary_color),
                    cursorColor = colorResource(R.color.primary_color)
                )
            )
            Spacer(Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(130.dp)
                        .height(45.dp)
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
                        .height(45.dp)
                        .pulsateClick(clickable = true, onClick = { onAdd(tagValue) })
                        .background(
                            color = colorResource(R.color.on_secondary_container),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.add),
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}