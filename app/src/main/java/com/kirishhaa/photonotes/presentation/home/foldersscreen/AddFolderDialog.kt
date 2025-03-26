package com.kirishhaa.photonotes.presentation.home.foldersscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddFolderDialog(
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var folderNameValue by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.size(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Add New Folder", fontSize = 24.sp)
            Spacer(Modifier.height(20.dp))
            TextField(
                value = folderNameValue,
                onValueChange = { folderNameValue = it },
                modifier = Modifier.width(250.dp),
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if (folderNameValue.isNotEmpty()) {
                            onAdd(folderNameValue)
                        } else {
                            Toast.makeText(
                                context,
                                "Folder name cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Add")
                }
            }
        }
    }
}