package com.fisun.md_stonetrack.data.repository

import com.fisun.md_stonetrack.data.api.ApiService
import com.fisun.md_stonetrack.data.model.FeedbackDTO
import com.fisun.md_stonetrack.domain.model.Feedback
import com.fisun.md_stonetrack.domain.repository.FeedbackRepository

class FeedbackRepositoryImpl(
    private val api: ApiService
) : FeedbackRepository {
    override suspend fun sendFeedback(token: String, feedback: Feedback) {
        val dto = FeedbackDTO(
            user_fullname = feedback.user_fullname,
            email = feedback.email,
            message = feedback.message,
            phone_number = feedback.phone_number,
            type_feedback = feedback.type_feedback,
            id_super_user = feedback.id_super_user
        )
        api.sendFeedback("Bearer $token", dto)
    }
}
