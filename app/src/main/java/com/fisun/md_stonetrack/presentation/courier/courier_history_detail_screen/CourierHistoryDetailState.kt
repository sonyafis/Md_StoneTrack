package com.fisun.md_stonetrack.presentation.courier.courier_history_detail_screen

import com.fisun.md_stonetrack.domain.model.Order

data class CourierHistoryDetailState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val error: String? = null
)
