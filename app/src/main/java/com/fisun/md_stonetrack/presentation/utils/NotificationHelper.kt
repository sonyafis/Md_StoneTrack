package com.fisun.md_stonetrack.presentation.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fisun.md_stonetrack.R
import kotlin.math.absoluteValue

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "orders_channel_id"
        private const val CHANNEL_NAME = "Обновления заказов"
        private const val CHANNEL_DESC = "Уведомления об изменении статуса заказа"
        private val VIBRATION_PATTERN = longArrayOf(0, 300, 200, 300)
    }

    private val notificationManager: NotificationManager? by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                vibrationPattern = VIBRATION_PATTERN
                setSound(null, null)
                enableLights(true)
            }

            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showStatusChangeNotification(orderNumber: String, newStatus: String) {
        try {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_order_notification)
                .setContentTitle("Статус заказа изменён")
                .setContentText("Заказ №$orderNumber: $newStatus")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(VIBRATION_PATTERN)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .build()

            val notificationId = generateNotificationId(orderNumber)
            notificationManager?.notify("orders_tag", notificationId, notification)

            Log.d("Notification", "Уведомление показано для заказа $orderNumber")
        } catch (e: Exception) {
            Log.e("Notification", "Ошибка показа уведомления", e)
        }
    }

    private fun generateNotificationId(orderNumber: String): Int {
        return orderNumber.hashCode().absoluteValue
    }
}