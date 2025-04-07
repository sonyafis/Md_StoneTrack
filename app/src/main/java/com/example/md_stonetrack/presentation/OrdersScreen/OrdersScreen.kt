package com.example.md_stonetrack.presentation.OrdersScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrdersScreen(token: String, viewModel: OrderViewModel = koinViewModel()) {
    viewModel.loadOrders(token)
    val orders = viewModel.orders.value

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(orders) { order ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Номер заказа: ${order.order_number}")
                    Text("Адрес: ${order.address}")
                    Text("Создан: ${order.created_at}")
                }
            }
        }
    }
}
