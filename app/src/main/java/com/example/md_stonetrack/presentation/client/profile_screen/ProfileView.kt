package com.example.md_stonetrack.presentation.client.profile_screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

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