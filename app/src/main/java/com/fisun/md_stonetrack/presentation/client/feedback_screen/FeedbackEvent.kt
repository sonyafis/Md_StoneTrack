package com.fisun.md_stonetrack.presentation.client.feedback_screen

sealed class FeedbackEvent {
    data class UpdateFullname(val value: String) : FeedbackEvent()
    data class UpdateEmail(val value: String) : FeedbackEvent()
    data class UpdatePhone(val value: String) : FeedbackEvent()
    data class UpdateMessage(val value: String) : FeedbackEvent()
    data class UpdateType(val value: String) : FeedbackEvent()
    object Submit : FeedbackEvent()
}