package com.fisun.md_stonetrack.domain.usecase

import com.fisun.md_stonetrack.domain.model.Feedback
import com.fisun.md_stonetrack.domain.repository.FeedbackRepository

class SendFeedbackUseCase(
    private val repository: FeedbackRepository
) {
    suspend operator fun invoke(token: String, feedback: Feedback) {
        repository.sendFeedback(token, feedback)
    }
}
