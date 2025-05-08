package com.fisun.md_stonetrack.domain.repository

import com.fisun.md_stonetrack.domain.model.Feedback

interface FeedbackRepository {
    suspend fun sendFeedback(token: String, feedback: Feedback)
}
