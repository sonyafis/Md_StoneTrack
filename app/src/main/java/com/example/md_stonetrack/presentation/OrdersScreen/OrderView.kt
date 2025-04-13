package com.example.md_stonetrack.presentation.OrdersScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.md_stonetrack.domain.repository.AuthRepository
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderView(
    viewModel: OrderViewModel = koinViewModel()
) {
    val orders by viewModel.orders
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else if (!error.isNullOrEmpty()) {
        Text(text = error!!)
    } else {
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
}