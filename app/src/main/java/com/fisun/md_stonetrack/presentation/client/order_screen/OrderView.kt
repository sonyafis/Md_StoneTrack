package com.fisun.md_stonetrack.presentation.client.order_screen

import AppFontFamily
import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fisun.md_stonetrack.R
import com.fisun.md_stonetrack.domain.model.Order
import com.fisun.md_stonetrack.presentation.navigation.BottomNavigationBar
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import com.fisun.md_stonetrack.presentation.utils.DateFormatter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun OrderView(navController: NavController, viewModel: OrderViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val userName = viewModel.userName
    val isRefreshing = viewModel.isRefreshing
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
        } else {
        }
    }
    var showSessionExpiredDialog by remember { mutableStateOf(false) }
    var showPermissionExplanation by remember { mutableStateOf(false) }

    if (showPermissionExplanation) {
        PermissionExplanationDialog(
            onDismiss = { showPermissionExplanation = false },
            onConfirm = {
                showPermissionExplanation = false
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        )
    }

    LaunchedEffect(state) {
        if (state is OrderState.SessionExpired) {
            showSessionExpiredDialog = true
        }
    }

    if (showSessionExpiredDialog) {
        AlertDialog(
            onDismissRequest = { showSessionExpiredDialog = false },
            title = { Text("Сессия истекла") },
            text = { Text("Для продолжения работы войдите снова") },
            confirmButton = {
                Button(onClick = {
                    showSessionExpiredDialog = false
                    navController.navigate("signin") {
                        popUpTo("orders_screen") { inclusive = true }
                    }
                }) {
                    Text("Войти")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val shouldShowExplanation = ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (shouldShowExplanation) {
                showPermissionExplanation = true
            } else {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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
                Text(
                    text = "Здравствуйте, $userName!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Image(
                    painter = painterResource(id = R.drawable.logo_home),
                    contentDescription = "Логотип"
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
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(colorResource(id = R.color.light_gray))
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text(text = "Заказы", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

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
                                    .verticalScroll(rememberScrollState())
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.empty_orders),
                                    contentDescription = null,
                                    modifier = Modifier.size(180.dp)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "У вас пока нет актуальных заказов",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Когда появятся, будут отображаться здесь.",
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Остальные заказы находятся в завершенных.",
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        is OrderState.Success -> {
                            val orders = (state as OrderState.Success).orders
                            Box(modifier = Modifier.fillMaxSize()) {
                                SwipeRefresh(
                                    state = rememberSwipeRefreshState(isRefreshing),
                                    onRefresh = { viewModel.refreshOrders() }
                                ) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp, vertical = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = PaddingValues(bottom = 16.dp)
                                    ) {
                                        items(orders) { order ->
                                            OrderCard(order = order) {
                                                navController.navigate("order_detail/${order.id_order}")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        is OrderState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Ошибка: ${(state as OrderState.Error).message}")
                            }
                        }

                        is OrderState.SessionExpired -> {
                            Box(Modifier.fillMaxSize())
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



@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, colorResource(id = R.color.purple)),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Заказ №${order.order_number}",
                fontSize = 15.sp,
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Divider(
                color = Color.LightGray,
                thickness = 3.dp,
                modifier = Modifier
                    .fillMaxWidth(0.427f)
                    .padding(vertical = 3.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Адрес: ${order.address}",
                fontSize = 13.sp,
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Создан: ${DateFormatter.formatDateTime(order.created_at)}",
                fontSize = 13.sp,
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Статус:",
                    fontSize = 13.sp,
                    fontFamily = AppFontFamily,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(colorResource(id = R.color.darkpurple))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = order.id_status.status_name,
                        fontSize = 13.sp,
                        fontFamily = AppFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PermissionExplanationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Разрешение уведомлений") },
        text = { Text("Мы уведомим вас об изменении статуса заказов") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Продолжить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

