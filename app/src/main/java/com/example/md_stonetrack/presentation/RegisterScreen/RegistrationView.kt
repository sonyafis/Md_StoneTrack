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
import androidx.compose.ui.focus.onFocusChanged
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

    // Дополнительное состояние для отслеживания, был ли выполнен ввод в поле
    var isUsernameTouched by remember { mutableStateOf(false) }
    var isEmailTouched by remember { mutableStateOf(false) }
    var isPasswordTouched by remember { mutableStateOf(false) }
    var isConfirmPasswordTouched by remember { mutableStateOf(false) }
    var isFirstNameTouched by remember { mutableStateOf(false) }
    var isLastNameTouched by remember { mutableStateOf(false) }
    var isPhoneNumberTouched by remember { mutableStateOf(false) }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val validationResult = viewModel.validateRegistrationFields(
        username,
        email,
        password,
        confirmPassword,
        first_name,
        last_name,
        phone_number
    )

    usernameError = if (isUsernameTouched) validationResult.errors["username"] else null
    emailError = if (isEmailTouched) validationResult.errors["email"] else null
    passwordError = if (isPasswordTouched) validationResult.errors["password"] else null
    confirmPasswordError = if (isConfirmPasswordTouched) validationResult.errors["confirmPassword"] else null
    firstNameError = if (isFirstNameTouched) validationResult.errors["firstName"] else null
    lastNameError = if (isLastNameTouched) validationResult.errors["lastName"] else null
    phoneNumberError = if (isPhoneNumberTouched) validationResult.errors["phoneNumber"] else null

    val passwordsMatch = password == confirmPassword
    val confirmPasswordFinalError = if (isConfirmPasswordTouched) {
        if (!passwordsMatch) "Пароли не совпадают" else confirmPasswordError
    } else null

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
                            label = "Имя",
                            errorMessage = firstNameError,
                            onTouched = { isFirstNameTouched = true }
                        )

                        RegistrationTextField(
                            value = last_name,
                            onValueChange = { last_name = it },
                            label = "Фамилия",
                            errorMessage = lastNameError,
                            onTouched = { isLastNameTouched = true }
                        )

                        RegistrationTextField(
                            value = phone_number,
                            onValueChange = { phone_number = it },
                            label = "Номер телефона",
                            keyboardType = KeyboardType.Phone,
                            errorMessage = phoneNumberError,
                            onTouched = { isPhoneNumberTouched = true }
                        )

                        RegistrationTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            keyboardType = KeyboardType.Email,
                            errorMessage = emailError,
                            onTouched = { isEmailTouched = true }
                        )

                        RegistrationTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Логин",
                            errorMessage = usernameError,
                            onTouched = { isUsernameTouched = true }
                        )

                        RegistrationTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Пароль",
                            isPassword = true,
                            errorMessage = passwordError,
                            onTouched = { isPasswordTouched = true }
                        )

                        RegistrationTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Подтверждение пароля",
                            isPassword = true,
                            errorMessage = confirmPasswordFinalError,
                            onTouched = { isConfirmPasswordTouched = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Кнопка (как в экране входа)
                    Button(
                        onClick = {
                            if (validationResult.isValid) {
                                viewModel.registerUser(
                                    username = username,
                                    email = email,
                                    password = password,
                                    first_name = first_name,
                                    last_name = last_name,
                                    phone_number = phone_number
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
    errorMessage: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onTouched: () -> Unit = {}
) {
    // Создаем переменную для отслеживания фокуса на поле
    var isFieldTouched by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontFamily = AppFontFamily, fontSize = 14.sp) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused || value.isNotEmpty()) {
                        isFieldTouched = true
                        onTouched()
                    }
                },
            shape = RoundedCornerShape(50),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = isFieldTouched && errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(id = R.color.purple),
                unfocusedBorderColor = colorResource(id = R.color.purple),
                errorBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = colorResource(id = R.color.darkpurple),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color.Black
            ),
            singleLine = true
        )
        // Показываем сообщение об ошибке только если поле было тронуто
        if (isFieldTouched && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}
