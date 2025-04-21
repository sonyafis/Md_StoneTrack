package com.example.md_stonetrack.domain.repository

import com.example.md_stonetrack.domain.model.Feedback

interface FeedbackRepository {
    suspend fun sendFeedback(token: String, feedback: Feedback)
}
