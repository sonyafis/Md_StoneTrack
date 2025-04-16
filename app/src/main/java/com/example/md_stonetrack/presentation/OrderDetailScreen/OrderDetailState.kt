package com.example.md_stonetrack.presentation.OrderDetailScreen

import com.example.md_stonetrack.domain.model.Order

data class OrderDetailState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val error: String? = null
)
