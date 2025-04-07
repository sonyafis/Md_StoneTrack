package com.example.md_stonetrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.md_stonetrack.presentation.OrdersScreen.OrdersScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzQ0MDQ5MTI2LCJpYXQiOjE3NDQwNDg1MjYsImp0aSI6ImM2MzcxNjcyYWEyYTQ4YjdiMmVkNWY4Mjg5OTJlNTYwIiwidXNlcl9pZCI6MX0.jgpQJS4cwaBxFrbxGaH0ohMR7I8DErmRmUiMFAmF6r0" // 🔒 Заменить на реальный токен, можно захардкодить для теста

        setContent {
            OrdersScreen(token)
        }
    }
}
