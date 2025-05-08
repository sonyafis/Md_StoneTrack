package com.fisun.md_stonetrack.presentation.client.history_detail_screen

import com.fisun.md_stonetrack.domain.model.Order

data class HistoryDetailState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val error: String? = null
)
