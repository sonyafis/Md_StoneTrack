package com.example.md_stonetrack.presentation.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.md_stonetrack.R

class NotificationHelper(private val context: Context) {

    private val channelId = "orders_channel_id"
    private val notificationId = 1

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Обновления заказов",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о изменении статусов заказов"
            }

            val manager = getSystemService(context, NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    fun showStatusChangeNotification(orderNumber: String) {
        try {
            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_order_notification)
                .setContentTitle("Статус изменён")
                .setContentText("Заказ №$orderNumber обновлён")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.notify(orderNumber.hashCode(), notification) // 👈 Уникальный ID для каждого заказа

            Log.d("Notification", "Уведомление показано для заказа $orderNumber")
        } catch (e: Exception) {
            Log.e("Notification", "Ошибка показа уведомления", e)
        }
    }
}