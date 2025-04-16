package com.example.md_stonetrack.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    @SuppressLint("SimpleDateFormat")
    fun formatDateTime(isoDate: String): String {
        return try {
            // Формат для входящей даты с миллисекундами и временной зоной
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
            val date = inputFormat.parse(isoDate)

            // Формат для вывода (день.месяц.год часы:минуты)
            val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault() // Используем локальную временную зону
            outputFormat.format(date ?: isoDate)
        } catch (e: Exception) {
            // Fallback: попробуем более простой парсинг, если первый формат не сработал
            try {
                val fallbackFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = fallbackFormat.parse(isoDate)
                SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date ?: isoDate)
            } catch (e: Exception) {
                isoDate // Если не удалось распарсить, возвращаем как есть
            }
        }
    }
}