package com.example.md_stonetrack.presentation.RegisterScreen

import AppFontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import com.example.md_stonetrack.R

@Composable
fun RegistrationView(
    navController: NavHostController,
    viewModel: RegistrationViewModel = koinViewModel()
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var first_name by remember { mutableStateOf("") }
    var last_name by remember { mutableStateOf("") }
    var phone_number by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Кнопка "Назад" (как в экране входа)
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp)
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Назад",
                    fontFamily = AppFontFamily,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight(0.9f)
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Логотип (точный размер как на экране входа)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    colorResource(id = R.color.darkpurple),
                                    shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "MD-STONE",
                                fontFamily = AppFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
//                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TRACK",
                            fontFamily = AppFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
//                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Заголовок (как в экране входа, но для регистрации)
                    Text(
                        text = "Регистрация",
                        fontFamily = AppFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Поля формы с такими же стилями как в экране входа
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RegistrationTextField(
                            value = first_name,
                            onValueChange = { first_name = it },
                            label = "Имя"
                        )

                        RegistrationTextField(
                            value = last_name,
                            onValueChange = { last_name = it },
                            label = "Фамилия"
                        )

                        RegistrationTextField(
                            value = phone_number,
                            onValueChange = { phone_number = it },
                            label = "Номер телефона",
                            keyboardType = KeyboardType.Phone
                        )

                        RegistrationTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            keyboardType = KeyboardType.Email
                        )

                        RegistrationTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Логин"
                        )

                        RegistrationTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Пароль",
                            isPassword = true
                        )

                        RegistrationTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Подтверждение пароля",
                            isPassword = true
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Кнопка (как в экране входа)
                    Button(
                        onClick = {
                            when {
                                username.isBlank() -> viewModel.setError("Введите логин")
                                email.isBlank() -> viewModel.setError("Введите email")
                                password.isBlank() -> viewModel.setError("Введите пароль")
                                confirmPassword.isBlank() -> viewModel.setError("Подтвердите пароль")
                                password != confirmPassword -> viewModel.setError("Пароли не совпадают")
                                password.length < 8 -> viewModel.setError("Пароль должен содержать минимум 8 символов")
                                !email.contains("@") -> viewModel.setError("Введите корректный email")
                                else -> viewModel.registerUser(
                                    username = username,
                                    email = email,
                                    password = password,
                                    first_name = first_name.ifBlank { null },
                                    last_name = last_name.ifBlank { null },
                                    phone_number = phone_number.ifBlank { null }
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.darkpurple)
                        )
                    ) {
                        Text(
                            text = "Зарегистрироваться",
                            fontFamily = AppFontFamily,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ссылка на вход (аналогично "Забыли пароль" в экране входа)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Уже есть аккаунт? ",
                            fontFamily = AppFontFamily,
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Войти",
                            fontFamily = AppFontFamily,
                            fontSize = 13.sp,
                            color = colorResource(id = R.color.darkpurple),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                navController.navigate("signin") {
                                    // Опциональные параметры навигации
                                    popUpTo("registration_screen") { inclusive = true } // Закрыть текущий экран
                                    launchSingleTop = true // Не создавать дубликаты экрана
                                }
                            }
                        )
                    }

                    // Обработка состояний
                    when (val state = uiState) {
                        is RegistrationViewModel.RegistrationUiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is RegistrationViewModel.RegistrationUiState.Error -> {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        is RegistrationViewModel.RegistrationUiState.Success -> {
                            LaunchedEffect(state) {
                                navController.popBackStack()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun RegistrationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = AppFontFamily, fontSize = 14.sp) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(50),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.purple),
            unfocusedBorderColor = colorResource(id = R.color.purple),
            cursorColor = colorResource(id = R.color.darkpurple),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp, // Оптимальный размер для ввода
            color = Color.Black // Гарантированная видимость
        ),
        singleLine = true // Гарантирует что текст не перекрывается
    )
}