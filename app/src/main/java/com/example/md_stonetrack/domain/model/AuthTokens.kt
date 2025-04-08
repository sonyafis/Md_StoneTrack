package com.example.md_stonetrack.domain.model

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
) {
    init {
        require(accessToken.isNotEmpty()) { "accessToken cannot be empty" }
        require(refreshToken.isNotEmpty()) { "refreshToken cannot be empty" }
    }
}