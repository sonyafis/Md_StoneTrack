package com.example.md_stonetrack.domain.model

sealed class AuthResult {
    data class Success(val tokens: AuthTokens) : AuthResult()
    data class Error(val message: String) : AuthResult()
}