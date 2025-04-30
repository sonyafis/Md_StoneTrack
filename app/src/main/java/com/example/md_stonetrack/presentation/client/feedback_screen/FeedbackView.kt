package com.example.md_stonetrack.presentation.client.feedback_screen

import AppFontFamily
import android.util.LayoutDirection
import android.util.Size
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.md_stonetrack.R
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.md_stonetrack.presentation.navigation.BottomNavigationBar
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp

@Composable
fun FeedbackView(
    navController: NavController,
    viewModel: FeedbackViewModel = koinViewModel()
) {
    val state = viewModel.state

    if (state.success) {
        FeedbackSuccessScreen(navController)
    } else {
        FeedbackFormScreen(navController, viewModel)
    }
}

@Composable
private fun FeedbackFormScreen(
    navController: NavController,
    viewModel: FeedbackViewModel
) {
    val state = viewModel.state

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
            // Шапка экрана
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Кнопка "Назад" слева
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { navController.popBackStack() }
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
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Логотип справа
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
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Заголовок с номером заказа
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(colorResource(id = R.color.light_gray))
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Обратная связь",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeedbackTextField(
                            label = "Имя и фамилия",
                            value = state.fullname,
                            onValueChange = { viewModel.onEvent(FeedbackEvent.UpdateFullname(it)) }
                        )

                        FeedbackTextField(
                            label = "Email",
                            value = state.email,
                            keyboardType = KeyboardType.Email,
                            onValueChange = { viewModel.onEvent(FeedbackEvent.UpdateEmail(it)) }
                        )

                        FeedbackTextField(
                            label = "Номер телефона",
                            value = state.phone,
                            keyboardType = KeyboardType.Phone,
                            onValueChange = { viewModel.onEvent(FeedbackEvent.UpdatePhone(it)) }
                        )

                        // Поле выбора типа обращения
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Тип обращения:",
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            FeedbackTypeDropdown(
                                selected = state.type,
                                onSelect = { viewModel.onEvent(FeedbackEvent.UpdateType(it)) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Поле сообщения
                        Column {
                            Text(
                                text = "Сообщение:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = state.message,
                                onValueChange = { viewModel.onEvent(FeedbackEvent.UpdateMessage(it)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                singleLine = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = colorResource(R.color.darkpurple),
                                    unfocusedBorderColor = colorResource(R.color.darkpurple)
                                )
                            )
                        }

                        // Кнопка отправки
                        Button(
                            onClick = { viewModel.onEvent(FeedbackEvent.Submit) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.darkpurple)
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(
                                text = "Отправить обращение",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                    }
                    }
                }
            }
        }

        // Нижняя навигация
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .background(colorResource(id = R.color.purple))
        ) {
            BottomNavigationBar(navController, selected = "feedback_screen")
        }
    }
}

@Composable
private fun FeedbackSuccessScreen(navController: NavController) {
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
            // Шапка экрана
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Кнопка "Назад" слева
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { navController.popBackStack() }
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
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Логотип справа
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
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Заголовок
                    Text(
                        text = "Новое обращение",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.purple),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Иконка успеха
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                        contentDescription = "Успех",
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Сообщение об успехе
                    Text(
                        text = "Ваше обращение было успешно создано и отправлено!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Ответ на него вы получите в течение 3 рабочих дней, на почту, указанную в форме обратной связи.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Кнопка "На главную"
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.purple)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "На главную",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Нижняя навигация
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .background(colorResource(id = R.color.purple))
        ) {
            BottomNavigationBar(navController, selected = "feedback_screen")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedbackTypeDropdown(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val currentSelected = if (selected.isEmpty()) "complaint" else selected
    val types = listOf("complaint", "suggestion", "inquiry", "praise", "issue", "request", "feedback")

    fun getDisplayName(type: String): String {
        return when(type) {
            "complaint" -> "Жалоба"
            "suggestion" -> "Предложение"
            "inquiry" -> "Вопрос"
            "praise" -> "Благодарность"
            "issue" -> "Проблема"
            "request" -> "Запрос"
            "feedback" -> "Отзыв"
            else -> type
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = getDisplayName(currentSelected),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.purple),
                unfocusedBorderColor = colorResource(R.color.purple),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White))
            {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = getDisplayName(type),
                                fontSize = 14.sp,
                                color = colorResource(R.color.purple))
                        },
                        onClick = {
                            onSelect(type)
                            expanded = false
                        }
                    )
                }
            }
    }
}

@Composable
fun FeedbackTextField(
    label: String,
    value: String,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = colorResource(R.color.purple)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.darkpurple),
            unfocusedBorderColor = colorResource(R.color.darkpurple),
            focusedLabelColor = colorResource(R.color.darkpurple),
            unfocusedLabelColor = colorResource(R.color.darkpurple),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        shape = RoundedCornerShape(30.dp)
    )
}