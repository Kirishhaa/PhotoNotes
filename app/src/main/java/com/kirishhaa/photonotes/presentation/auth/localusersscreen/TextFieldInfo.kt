package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirishhaa.photonotes.R

@Composable
fun TextFieldInfo(title: String, value: String, error: Boolean, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
            label = {
                Text(text = title,
                    fontFamily = FontFamily(Font(R.font.comic)),
                    color = colorResource(R.color.primary_color)
                )
            },

            value = value,
            onValueChange = onValueChanged,
            textStyle = TextStyle.Default.copy(
                fontSize = 14.sp,
            ),
            isError = error,
            maxLines = 1,
            modifier = Modifier
                .width(140.dp)
                .height(60.dp)
        )
}