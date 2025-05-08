package com.fisun.md_stonetrack.presentation.client.order_detail_screen

import com.fisun.md_stonetrack.domain.model.Order

data class OrderDetailState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val error: String? = null
)
