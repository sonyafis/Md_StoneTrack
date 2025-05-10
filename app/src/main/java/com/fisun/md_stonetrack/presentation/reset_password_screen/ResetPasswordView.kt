package com.fisun.md_stonetrack.presentation.reset_password_screen

import com.fisun.md_stonetrack.presentation.theme.AppFontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fisun.md_stonetrack.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResetPasswordView(
    navController: NavController,
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    val email by viewModel.email
    val emailError by viewModel.emailError
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple))
    )
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp)
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Назад",
                    fontFamily = AppFontFamily,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight(0.92f)
                    .padding(bottom = 25.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    colorResource(id = R.color.darkpurple),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "MD-STONE",
                                fontSize = 30.sp,
                                fontFamily = AppFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TRACK",
                            fontFamily = AppFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Сброс пароля",
                        fontFamily = AppFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { viewModel.onEmailChange(it) },
                            label = { Text("Email", fontFamily = AppFontFamily) },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else colorResource(id = R.color.purple),
                                unfocusedBorderColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else colorResource(id = R.color.purple),
                                cursorColor = colorResource(id = R.color.darkpurple)
                            ),
                            isError = emailError != null
                        )

                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.onResetPassword() },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.darkpurple)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        enabled = email.isNotBlank() && uiState !is ResetPasswordViewModel.ResetPasswordUiState.Loading
                    ) {
                        when (uiState) {
                            is ResetPasswordViewModel.ResetPasswordUiState.Loading -> {
                                CircularProgressIndicator(color = Color.White)
                            }

                            else -> {
                                Text(
                                    text = "Отправить ссылку",
                                    fontFamily = AppFontFamily,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (uiState) {
                        is ResetPasswordViewModel.ResetPasswordUiState.Success -> {
                            Text(
                                text = "Ссылка для сброса пароля отправлена на ваш email",
                                color = colorResource(id = R.color.darkpurple),
                                fontFamily = AppFontFamily
                            )
                        }

                        is ResetPasswordViewModel.ResetPasswordUiState.Error -> {
                            Text(
                                text = (uiState as ResetPasswordViewModel.ResetPasswordUiState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = AppFontFamily
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}