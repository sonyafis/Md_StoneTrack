package com.example.md_stonetrack.presentation.OrdersScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.md_stonetrack.R
import com.example.md_stonetrack.domain.model.Order
import com.example.md_stonetrack.presentation.navigation.BottomNavigationBar
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun OrderView(navController: NavController, viewModel: OrderViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val userName = viewModel.userName

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple))
    ) {
        // Основной контент экрана
        when (state) {
            is OrderState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is OrderState.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp)
                ) {
                    // Шапка с приветствием и логотипом
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Здравствуйте, $userName!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Image(
                            painter = painterResource(id = R.drawable.logo_home),
                            contentDescription = "Логотип",
//                            modifier = Modifier.size(70.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Белый блок с сообщением
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.87f)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Активные заказы:",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                            fontSize = 25.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.empty_orders),
                            contentDescription = null,
                            modifier = Modifier.size(180.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Здесь будут ваши заказы",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                            fontSize = 25.sp
                        )
                    }
                }
            }
            is OrderState.Success -> {
                val orders = (state as OrderState.Success).orders
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(bottom = 80.dp), // Отступ для навигации
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        OrderCard(order = order, onClick = {
                            navController.navigate("order_detail/${order.id_order}")
                        })
                    }
                }
            }
            is OrderState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Ошибка: ${(state as OrderState.Error).message}",
                        color = Color.White
                    )
                }
            }
        }

        // Навигационная панель внизу
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            BottomNavigationBar(navController, selected = "orders_screen")
        }
    }
}

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Заказ №${order.order_number}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Адрес: ${order.address}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Создан: ${order.created_at}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Статус: ${order.id_status.status_name}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
