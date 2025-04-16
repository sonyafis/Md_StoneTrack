package com.example.md_stonetrack.presentation.ProfileScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileView(
    viewModel: ProfileViewModel,
    onLogoutSuccess: () -> Unit
) {
    Button(onClick = {
        viewModel.logout()
        onLogoutSuccess()
    }) {
        Text("Выйти из аккаунта")
    }
}