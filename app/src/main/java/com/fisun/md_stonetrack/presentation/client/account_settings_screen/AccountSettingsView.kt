package com.fisun.md_stonetrack.presentation.client.account_settings_screen

import com.fisun.md_stonetrack.presentation.theme.AppFontFamily
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fisun.md_stonetrack.R
import com.fisun.md_stonetrack.presentation.navigation.BottomNavigationBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountSettingsView(
    navController: NavController,
    viewModel: AccountSettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple))
    ) {
        when (state) {
            is AccountSettingsViewModel.AccountSettingsState.Loading -> {
                LoadingView()
            }

            is AccountSettingsViewModel.AccountSettingsState.Error -> {
                ErrorView((state as AccountSettingsViewModel.AccountSettingsState.Error).message)
            }

            is AccountSettingsViewModel.AccountSettingsState.Success -> {
                val userData =
                    (state as AccountSettingsViewModel.AccountSettingsState.Success).userData
                AccountSettingsContent(
                    navController = navController,
                    userData = userData,
                    onSaveClick = { updatedData ->
                        viewModel.updateUserData(updatedData)
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountSettingsContent(
    navController: NavController,
    userData: UserData,
    onSaveClick: (UserData) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple))
    ) {
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
                    modifier = Modifier.clickable { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "К профилю",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "К профилю",
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
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
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
                            Text(
                                text = "Настройки аккаунта",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        colorResource(id = R.color.purple),
                                        colorResource(id = R.color.darkpurple)
                                    )
                                ),
                                shape = CircleShape
                            )
                            .border(
                                width = 2.dp,
                                color = colorResource(id = R.color.darkpurple),
                                shape = CircleShape
                            )
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userData.name.first().toString().uppercase(),
                            color = Color.White,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "Личные данные:",
                        fontFamily = AppFontFamily,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.purple),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    ProfileSettingItem(
                        label = "ФИО",
                        value = userData.fullName,
                        showEditButton = false
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileSettingItem(
                        label = "Номер телефона",
                        value = userData.phone,
                        showEditButton = false
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileSettingItem(
                        label = "Email",
                        value = userData.email,
                        showEditButton = false
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileSettingItem(
                        label = "Логин",
                        value = userData.login,
                        showEditButton = false
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileSettingItem(
                        label = "Пароль",
                        value = "",
                        showEditButton = true,
                        editButtonText = "изменить пароль",
                        onEditClick = { navController.navigate("change_password_screen") }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, colorResource(id = R.color.purple)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Отмена",
                                color = colorResource(id = R.color.purple),
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                showDialog = true
                                onSaveClick(userData)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.purple)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Сохранить",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            if (showDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false },
                                    title = { Text("Подтверждение") },
                                    text = { Text("Вы уверены, что хотите сохранить изменения?") },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                onSaveClick(userData)
                                                showDialog = false
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Данные сохранены")
                                                }
                                            }
                                        ) {
                                            Text("Да")
                                        }
                                    },
                                    dismissButton = {
                                        OutlinedButton(
                                            onClick = { showDialog = false }
                                        ) {
                                            Text("Отмена")
                                        }
                                    }
                                )
                            }
                        }
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
fun ProfileSettingItem(
    label: String,
    value: String,
    showEditButton: Boolean = false,
    editButtonText: String = "",
    onEditClick: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "$label:",
                    fontFamily = AppFontFamily,
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = value,
                    fontFamily = AppFontFamily,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.black)
                )
            }

            if (showEditButton) {
                Surface(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(50.dp),
                    color = colorResource(id = R.color.purple),
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .clickable(onClick = onEditClick)
                ) {
                    Text(
                        text = editButtonText,
                        fontFamily = AppFontFamily,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 25.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = colorResource(id = R.color.light_gray),
            thickness = 1.dp
        )
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
private fun ErrorView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}