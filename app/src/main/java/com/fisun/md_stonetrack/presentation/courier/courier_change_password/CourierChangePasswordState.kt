package com.fisun.md_stonetrack.presentation.courier.courier_change_password

sealed class CourierChangePasswordState {
    object Idle : CourierChangePasswordState()
    object Loading : CourierChangePasswordState()
    object Success : CourierChangePasswordState()
    data class Error(val message: String) : CourierChangePasswordState()
}
