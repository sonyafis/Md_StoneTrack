package com.example.md_stonetrack.presentation.SignInScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInView(navController: NavController, viewModel: SignInViewModel = koinViewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { viewModel.login(username, password) }) {
            Text("Войти")
        }

        when (uiState) {
            is SignInViewModel.AuthUiState.Loading -> {
                CircularProgressIndicator()
            }
            is SignInViewModel.AuthUiState.Error -> {
                Text(
                    text = (uiState as SignInViewModel.AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is SignInViewModel.AuthUiState.Success -> {
                LaunchedEffect(Unit) {
                    // Временная проверка по логину
                    if (username.lowercase().contains("courier")) {
                        navController.navigate("courier_screen")
                    } else {
                        navController.navigate("orders_screen")
                    }
                }
            }
            else -> {}
        }
    }
}
