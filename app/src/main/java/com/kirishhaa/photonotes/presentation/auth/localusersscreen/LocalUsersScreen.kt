package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick
import com.kirishhaa.photonotes.presentation.EditButton


@Composable
fun LocalUsersScreen(
    viewModel: LocalUsersViewModel = viewModel(factory = LocalUsersViewModel.Factory),
    toHomeScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val signInState = state.signInDialogState
    val signUpState = state.signUpDialogState

    val context = LocalContext.current
    LaunchedEffect(0) {
        viewModel.events.collect { event ->
            when (event.getValue()) {
                LocalUsersEvent.WrongPasswordException -> {
                    Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show()
                }

                LocalUsersEvent.WrongLoginException -> {
                    Toast.makeText(context, "Wrong login", Toast.LENGTH_SHORT).show()
                }

                LocalUsersEvent.WrongUsernameException -> {
                    Toast.makeText(context, "Wrong username", Toast.LENGTH_SHORT).show()
                }

                LocalUsersEvent.EnteredUserExist -> toHomeScreen()

                LocalUsersEvent.CloseApp -> {
                    (context as? Activity)?.finish()
                }

                null -> {}
            }
        }
    }

    BackHandler { viewModel.handleBackNavigation() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
            .background(
                color = colorResource(R.color.background_main_color)
            )
    ) {

        when {
            state.screenState.loading -> CircularProgressIndicator()
            else -> LocalUsersScreen(
                state = state.screenState,
                clickable = signInState == null && signUpState == null,
                onUserClick = viewModel::showSignInDialog,
                onEdit = viewModel::onEdit
            )
        }

        AnimatedVisibility(
            visible = signInState != null
        ) {
            if (signInState != null) {
                SignInUserDialog(
                    stateUI = signInState,
                    onSignIn = viewModel::onSignIn,
                    onDismiss = viewModel::hideSignInDialog
                )
            }
        }

        AnimatedVisibility(
            visible = signUpState != null
        ) {
            if (signUpState != null) {
                SignUpUserDialog(
                    state = signUpState,
                    onSignUp = viewModel::onSignUp,
                    onDismiss = viewModel::hideSignUpDialog
                )
            }
        }

    }
}

@Composable
private fun BoxScope.LocalUsersScreen(
    state: LocalUsersScreenStateUI,
    clickable: Boolean,
    onUserClick: (LocalUserUI) -> Unit,
    onEdit: () -> Unit
) {
    if(state.users?.isEmpty() == true) {
        Text(
            text = "There aren't any users yet.\nJust create one!",
            fontFamily = FontFamily(Font(R.font.comic)),
            fontSize = 32.sp,
            color = colorResource(R.color.primary_color),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
                .padding(horizontal = 20.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(state.users ?: emptyList()) { user ->
                LocalUserView(
                    localUser = user,
                    clickable = clickable,
                    onClick = { onUserClick(user) })
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = -12.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.width(200.dp).height(50.dp)
                .pulsateClick(clickable, onEdit)
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Color.Black
                )
                .background(
                    color = colorResource(R.color.primary_container),
                    shape = RoundedCornerShape(24.dp)
                )

        ) {
            Text(
                text = "Create a user",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.comic)),
                color = colorResource(R.color.primary_color),
            )
        }
    }
}