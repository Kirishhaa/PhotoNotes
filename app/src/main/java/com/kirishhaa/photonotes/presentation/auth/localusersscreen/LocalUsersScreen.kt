package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.presentation.EditButton
import com.kirishhaa.photonotes.clickeffects.pulsateClick


@Composable
fun LocalUsersScreen(viewModel: LocalUsersViewModel = viewModel(factory = LocalUsersViewModel.Factory),
                     toHomeScreen: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val signInState = state.signInDialogState
    val signUpState = state.signUpDialogState

    val context = LocalContext.current
    LaunchedEffect(0) {
        viewModel.events.collect { event ->
            when(event.getValue()) {
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

        if(signInState != null) {
            SignInUserDialog(
                stateUI = signInState,
                onSignIn = viewModel::onSignIn,
                onDismiss = viewModel::hideSignInDialog
            )
        }

        if(signUpState != null) {
            SignUpUserDialog(state = signUpState, onSignUp = viewModel::onSignUp, onDismiss = viewModel::hideSignUpDialog)
        }

    }
}

@Composable
private fun BoxScope.LocalUsersScreen(state: LocalUsersScreenStateUI, clickable: Boolean, onUserClick: (LocalUserUI) -> Unit, onEdit: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.users ?: emptyList()) { user ->
            LocalUserView(localUser = user, clickable = clickable, onClick = {  onUserClick(user) })
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .width(48.dp)
            .height(60.dp)
            .align(Alignment.BottomCenter)) {
        EditButton(
            modifier = Modifier.pulsateClick(clickable = clickable, onClick = onEdit)
        )
    }
}