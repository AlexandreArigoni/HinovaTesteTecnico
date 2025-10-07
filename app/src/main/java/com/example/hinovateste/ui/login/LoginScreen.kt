package com.example.hinovateste.ui.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.hinovateste.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.userName) {
        if (state.userName != null) {
            onSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hinova_logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = state.cpf,
                onValueChange = viewModel::onCpfChange,
                label = { Text(stringResource(R.string.cpf)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.senha,
                onValueChange = viewModel::onPasswordChange,
                label = { Text(stringResource(R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            state.userName?.let {
                Text(
                    text = stringResource(R.string.welcome_message, it),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}