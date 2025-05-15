package com.fisun.md_stonetrack.presentation.client.change_password

import com.fisun.md_stonetrack.presentation.theme.AppFontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fisun.md_stonetrack.R
import com.fisun.md_stonetrack.presentation.navigation.BottomNavigationBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordView(
    navController: NavController,
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.message) {
        viewModel.message?.let { message ->
            if (message == "Пароль успешно изменён") {
                showSuccessDialog = true
            }
            viewModel.message = null
        }
    }

    Scaffold(
        containerColor = colorResource(id = R.color.purple)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                            contentDescription = "К настройкам",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "К настройкам",
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
                                    text = "Смена пароля",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        PasswordTextField(
                            label = "Старый пароль",
                            value = viewModel.current_password,
                            onValueChange = { viewModel.current_password = it },
                            errorMessage = viewModel.passwordErrors["current_password"]
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PasswordTextField(
                            label = "Новый пароль",
                            value = viewModel.new_password,
                            onValueChange = { viewModel.new_password = it },
                            errorMessage = viewModel.passwordErrors["new_password"]
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PasswordTextField(
                            label = "Подтвердите пароль",
                            value = viewModel.re_new_password,
                            onValueChange = { viewModel.re_new_password = it },
                            errorMessage = viewModel.passwordErrors["re_new_password"]
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                viewModel.onChangePassword()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("changePasswordButton")
                                .height(50.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.darkpurple)
                            ),
                            enabled = !viewModel.isLoading
                        ) {
                            if (viewModel.isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text("Изменить пароль", fontSize = 20.sp, color = Color.White)
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

            if (showSuccessDialog) {
                SuccessPasswordDialog(
                    onDismiss = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun PasswordTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth().testTag("${label}Field"),
            shape = RoundedCornerShape(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (errorMessage == null) colorResource(id = R.color.purple) else MaterialTheme.colorScheme.error,
                unfocusedBorderColor = if (errorMessage == null) colorResource(id = R.color.purple) else MaterialTheme.colorScheme.error,
                errorBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = colorResource(id = R.color.darkpurple),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        icon,
                        contentDescription = "Видимость пароля",
                        tint = colorResource(id = R.color.darkpurple)
                    )
                }
            },
            isError = errorMessage != null
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp).testTag("${label}ErrorText")
            )
        }
    }
}

@Composable
private fun SuccessPasswordDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.darkpurple)
                )
            ) {
                Text("Вернуться к настройкам")
            }
        },
        title = {
            Text(
                text = "Ваш пароль был успешно изменен!",
                fontWeight = FontWeight.Bold
            )
        },
        containerColor = Color.White
    )
}