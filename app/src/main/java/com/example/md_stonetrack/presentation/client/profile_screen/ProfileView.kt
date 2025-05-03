package com.example.md_stonetrack.presentation.client.profile_screen

import AppFontFamily
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.md_stonetrack.R
import com.example.md_stonetrack.presentation.navigation.BottomNavigationBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileView(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is ProfileViewModel.ProfileEvent.NavigateToAbout -> {
                navController.navigate("about_us_screen")
                viewModel.resetNavigationEvent()
            }
            is ProfileViewModel.ProfileEvent.NavigateToSettings -> {
                navController.navigate("")
                viewModel.resetNavigationEvent()
            }
            is ProfileViewModel.ProfileEvent.Logout -> {
                viewModel.logout()
                navController.navigate("start_screen") {
                    popUpTo(0)
                }
                viewModel.resetNavigationEvent()
            }
            is ProfileViewModel.ProfileEvent.DeleteAccount -> {
                viewModel.deleteAccount()
                navController.navigate("start_screen") {
                    popUpTo(0)
                }
                viewModel.resetNavigationEvent()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple))
    ) {
        when (state) {
            is ProfileViewModel.ProfileState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is ProfileViewModel.ProfileState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (state as ProfileViewModel.ProfileState.Error).message,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
            is ProfileViewModel.ProfileState.Success -> {
                val userName = (state as ProfileViewModel.ProfileState.Success).userName

                ProfileContent(
                    navController = navController,
                    userName = userName,
                    onAboutClick = { viewModel.onEvent(ProfileViewModel.ProfileEvent.NavigateToAbout) },
                    onSettingsClick = { viewModel.onEvent(ProfileViewModel.ProfileEvent.NavigateToSettings) },
                    onLogoutClick = { viewModel.onEvent(ProfileViewModel.ProfileEvent.Logout) },
                    onDeleteClick = { viewModel.onEvent(ProfileViewModel.ProfileEvent.DeleteAccount) }
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    navController: NavController,
    userName: String,
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Выход из аккаунта") },
            text = { Text("Вы уверены, что хотите выйти из аккаунта?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple))
                ) {
                    Text("Выйти")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удаление аккаунта") },
            text = { Text("Вы уверены, что хотите удалить аккаунт? Это действие нельзя отменить.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.red))
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.purple))) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { navController.navigate("orders_screen") }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "На главную",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "На главную",
                        fontFamily = AppFontFamily,
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.logo_home),
                    contentDescription = "Логотип",
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(32.dp),
                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(colorResource(id = R.color.light_gray))
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text(text = "Личный кабинет", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                color = colorResource(id = R.color.light_gray),
                                shape = CircleShape
                            )
                            .border(
                                width = 2.dp,
                                color = colorResource(id = R.color.purple),
                                shape = CircleShape
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.first().toString().uppercase(),
                            color = colorResource(id = R.color.purple),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Здравствуйте, $userName!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.black),
                        modifier = Modifier.padding(top = 36.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileMenuItem(
                            text = "О нас",
                            onClick = onAboutClick
                        )

                        ProfileMenuItem(
                            text = "Настройки аккаунта",
                            onClick = onSettingsClick
                        )

                        ProfileMenuItem(
                            text = "Выйти из аккаунта",
                            onClick = { showLogoutDialog = true }
                        )

                        ProfileMenuItem(
                            text = "Удалить аккаунт",
                            onClick = { showDeleteDialog = true },
                            textColor = colorResource(id = R.color.darkpurple)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .background(colorResource(id = R.color.purple))
        ) {
            BottomNavigationBar(navController, selected = "profile_screen")
        }
    }
}

@Composable
fun ProfileMenuItem(
    text: String,
    onClick: () -> Unit,
    textColor: Color = colorResource(id = R.color.purple)
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.purple)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                color = textColor,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}