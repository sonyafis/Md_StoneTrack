package com.fisun.md_stonetrack.presentation.sign_in_screen

import AppFontFamily
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fisun.md_stonetrack.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInView(navController: NavController, viewModel: SignInViewModel = koinViewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    var isTermsAccepted by remember { mutableStateOf(false) }
    var isTermsTouched by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Назад",
                    fontFamily = AppFontFamily,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
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
                        .verticalScroll(rememberScrollState())
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
                                .background(colorResource(id = R.color.darkpurple), shape = RoundedCornerShape(4.dp))
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
                            fontSize = 28.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Вход",
                        fontFamily = AppFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Логин", fontFamily = AppFontFamily) },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.purple),
                            unfocusedBorderColor = colorResource(id = R.color.purple),
                            cursorColor = colorResource(id = R.color.darkpurple)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Пароль", fontFamily = AppFontFamily) },
                        shape = RoundedCornerShape(50),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.purple),
                            unfocusedBorderColor = colorResource(id = R.color.purple),
                            cursorColor = colorResource(id = R.color.darkpurple)
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
                                    tint = colorResource(id = R.color.darkpurple)
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Забыли пароль?",
                        fontFamily = AppFontFamily,
                        color = colorResource(id = R.color.darkpurple),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { navController.navigate("reset_password") }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isTermsAccepted = !isTermsAccepted
                                isTermsTouched = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isTermsAccepted,
                            onCheckedChange = {
                                isTermsAccepted = it
                                isTermsTouched = true
                            }
                        )
                        val context = LocalContext.current

                        val annotatedText = buildAnnotatedString {
                            append("Я соглашаюсь с ")

                            pushStringAnnotation(
                                tag = "TOS",
                                annotation = "https://docs.google.com/document/d/1AKRq43puxtA5UEcm00QrOwjKmgORwuv1OdHRdeXvC98/edit?usp=sharing"
                            )
                            withStyle(style = SpanStyle(color = colorResource(id = R.color.darkpurple), fontWeight = FontWeight.Bold)) {
                                append("пользовательским соглашением")
                            }
                            pop()

                            append(" и ")

                            pushStringAnnotation(
                                tag = "Privacy",
                                annotation = "https://docs.google.com/document/d/1rkNeE6-mZqJFvMPk8E_gcYLYOGSJt5oQZr0-PZa5CAs/edit?usp=sharing"
                            )
                            withStyle(style = SpanStyle(color = colorResource(id = R.color.darkpurple), fontWeight = FontWeight.Bold)) {
                                append("политикой конфиденциальности")
                            }
                            pop()
                        }

                        ClickableText(
                            text = annotatedText,
                            style = TextStyle(fontSize = 13.sp, fontFamily = AppFontFamily),
                            modifier = Modifier.padding(start = 8.dp),
                            onClick = { offset ->
                                annotatedText.getStringAnnotations(start = offset, end = offset)
                                    .firstOrNull()?.let { annotation ->
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                        context.startActivity(intent)
                                    }
                            }
                        )
                    }
                    if (!isTermsAccepted && isTermsTouched) {
                        Text(
                            text = "Вы должны принять условия соглашения",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.login(username, password) },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.darkpurple)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = "Войти",
                            fontFamily = AppFontFamily,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (uiState) {
                        is SignInViewModel.AuthUiState.Loading -> CircularProgressIndicator()
                        is SignInViewModel.AuthUiState.Error -> Text(
                            text = (uiState as SignInViewModel.AuthUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        is SignInViewModel.AuthUiState.Success -> {
                            LaunchedEffect(Unit) {
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
        }
    }
}

