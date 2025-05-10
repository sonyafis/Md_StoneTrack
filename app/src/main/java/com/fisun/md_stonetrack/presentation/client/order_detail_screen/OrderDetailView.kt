package com.fisun.md_stonetrack.presentation.client.order_detail_screen

import com.fisun.md_stonetrack.presentation.theme.AppFontFamily
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fisun.md_stonetrack.R
import com.fisun.md_stonetrack.presentation.utils.DateFormatter
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import com.fisun.md_stonetrack.presentation.navigation.BottomNavigationBar

@Composable
fun OrderDetailView(
    navController: NavController,
    id_order: Int?,
    viewModel: OrderDetailViewModel = koinViewModel()
) {
    if (id_order == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Ошибка: id заказа не передан")
        }
        return
    }

    val orderState by viewModel.orderState.collectAsState()

    LaunchedEffect(id_order) {
        viewModel.loadOrderDetail(id_order)
    }

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
                when {
                    orderState.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    orderState.error != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ошибка: ${orderState.error}")
                        }
                    }

                    orderState.order != null -> {
                        val order = orderState.order
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
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
                                        text = "Заказ №${order?.order_number}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(2.dp, colorResource(id = R.color.purple)),
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = "Адрес: ${order?.address ?: "Нет данных"}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "Создан: ${DateFormatter.formatDateTime(order?.created_at ?: "Нет данных")}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Статус: ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = AppFontFamily
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(30.dp))
                                                .background(colorResource(id = R.color.darkpurple))
                                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = order?.id_status?.status_name
                                                    ?: "Нет данных",
                                                fontSize = 16.sp,
                                                fontFamily = AppFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Text(
                                        text = "Назначенный курьер: ${order?.id_courier?.let { "${it.first_name} ${it.last_name}" } ?: "Не назначен"}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "Номер курьера для связи: ${order?.id_courier?.phone_number ?: "Нет данных"}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "Описание:",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                    )
                                    Text(
                                        text = order?.description ?: "Нет описания",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                }
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
            BottomNavigationBar(navController, selected = "orders_screen")
        }
    }
}