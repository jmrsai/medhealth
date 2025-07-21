package com.jmr.medhealth.ui.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmr.medhealth.data.repository.ChatMessage
import com.jmr.medhealth.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val initialMessages = listOf(
            ChatMessage("Hello! I'm your AI Health Agent. How can I help you today?", isFromUser = false)
        )
        _uiState.value = _uiState.value.copy(messages = initialMessages)
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = ChatMessage(text, isFromUser = true)
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isLoading = true
        )

        viewModelScope.launch {
            val result = chatRepository.sendMessage(text)
            val botMessage = result.fold(
                onSuccess = { ChatMessage(it, isFromUser = false) },
                onFailure = { ChatMessage("Sorry, an error occurred: ${it.message}", isFromUser = false) }
            )
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + botMessage,
                isLoading = false
            )
        }
    }
}