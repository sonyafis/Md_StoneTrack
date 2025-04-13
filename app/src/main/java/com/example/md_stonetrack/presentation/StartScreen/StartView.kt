package com.example.mdstonetrack.presentation.StartScreen.StartView

import AppFontFamily
import MdStoneTrackTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.md_stonetrack.R
import com.example.md_stonetrack.presentation.StartScreen.StartViewModel
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun StartView(
    viewModel: StartViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is StartViewModel.NavigationEvent.NavigateToLogin -> onNavigateToLogin()
                is StartViewModel.NavigationEvent.NavigateToRegister -> onNavigateToRegister()
            }
        }
    }

    MdStoneTrackTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.purple))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .padding(bottom = 25.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(32.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Верхняя часть с увеличенными текстами
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Заголовок с увеличенным шрифтом
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = colorResource(id = R.color.darkpurple),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "MD-STONE",
//                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 30.sp,
                                fontFamily = AppFontFamily,
                                fontWeight = FontWeight.Bold,// Увеличенный размер
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TRACK",
//                            style = MaterialTheme.typography.headlineLarge,
                            fontSize = 30.sp,
                            fontFamily = AppFontFamily,
                            fontWeight = FontWeight.Bold,// Увеличенный размер
                            color = Color.Black
                        )
                    }

                    // Приветствие с увеличенным шрифтом
                    Text(
                        text = "Здравствуйте!",
                        style = MaterialTheme.typography.headlineSmall, // Увеличенный размер
                        fontFamily = AppFontFamily,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 15.dp), // Увеличенный отступ
                        textAlign = TextAlign.Center
                    )

                    // Описание с увеличенным шрифтом
                    Text(
                        text = "Удобное отслеживание заказов мебели — всегда под рукой!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = AppFontFamily,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 49.dp), // Увеличенный отступ
                        textAlign = TextAlign.Center
                    )

                    // Изображение
                    Image(
                        painter = painterResource(id = R.drawable.package_delivered),
                        contentDescription = "Доставка",
                        modifier = Modifier
                            .fillMaxWidth(0.9f) // Уменьшенная ширина
                            .height(200.dp), // Увеличенная высота
                        contentScale = ContentScale.Fit
                    )
                }

                // Кнопки (уменьшенные по ширине)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp) // Боковые отступы
                        .padding(bottom = 40.dp), // Боковые отступы
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { viewModel.onLoginClick() },
                        modifier = Modifier
                            .fillMaxWidth(0.9f) // Уменьшенная ширина
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.darkpurple)
                        )
                    ) {
                        Text(
                            text = "Войти",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = AppFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { viewModel.onRegisterClick() },
                        modifier = Modifier
                            .fillMaxWidth(0.9f) // Уменьшенная ширина
                            .height(48.dp),
                        border = BorderStroke(1.dp, colorResource(id = R.color.darkpurple)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorResource(id = R.color.darkpurple)
                        )
                    ) {
                        Text(
                            text = "Регистрация",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = AppFontFamily,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}
