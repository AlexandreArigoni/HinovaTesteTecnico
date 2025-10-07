package com.example.hinovateste.ui.recommendation

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hinovateste.R
import com.example.hinovateste.data.remote.ApiService
import com.example.hinovateste.data.repository.AutoShopRepository
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun RecommendationTab() {
    val repository = remember { AutoShopRepository(ApiService.create()) }

    val context = LocalContext.current.applicationContext as Application

    val viewModel: RecommendationViewModel = viewModel(
        factory = RecommendationViewModelFactory(repository, context)
    )

    val state by viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(R.string.invite_friends), style = MaterialTheme.typography.titleSmall)

        OutlinedTextField(
            value = state.nomeAssociado ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(nomeAssociado = it) },
            label = { Text(stringResource(R.string.label_associated_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.cpfAssociado ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(cpfAssociado = it) },
            label = { Text(stringResource(R.string.label_associated_cpf)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.emailAssociado ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(emailAssociado = it) },
            label = { Text(stringResource(R.string.label_associated_email)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.telefoneAssociado ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(telefoneAssociado = it) },
            label = { Text(stringResource(R.string.label_associated_phone)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.placa ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(placa = it) },
            label = { Text(stringResource(R.string.label_vehicle_plate)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.nomeAmigo ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(nomeAmigo = it) },
            label = { Text(stringResource(R.string.label_name_friend)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.emailAmigo ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(emailAmigo = it) },
            label = { Text(stringResource(R.string.label_email_friend)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.telefoneAmigo ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(telefoneAmigo = it) },
            label = { Text(stringResource(R.string.label_friend_phone)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.observacao ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(observacao = it) },
            label = { Text(stringResource(R.string.label_notice)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.remetente ?: "",
            onValueChange = { viewModel.uiState.value = state.copy(remetente = it) },
            label = { Text(stringResource(R.string.label_referent)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.error != null) {
            Text(state.error ?: "", color = Color.Red)
        }

        if (state.success != null) {
            Text(state.success ?: "", color = Color.Green)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.sendRecommendation() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(R.string.send))
        }

    }
}