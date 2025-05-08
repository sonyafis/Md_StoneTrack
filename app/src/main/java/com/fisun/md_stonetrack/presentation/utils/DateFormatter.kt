package com.fisun.md_stonetrack.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    @SuppressLint("SimpleDateFormat")
    fun formatDateTime(isoDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(isoDate)

            val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("Europe/Moscow")
            outputFormat.format(date ?: isoDate)
        } catch (e: Exception) {
            try {
                val fallbackFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                fallbackFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = fallbackFormat.parse(isoDate)
                SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date ?: isoDate)
            } catch (e: Exception) {
                isoDate
            }
        }
    }
}