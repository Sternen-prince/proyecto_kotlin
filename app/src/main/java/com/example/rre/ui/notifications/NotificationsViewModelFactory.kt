package com.example.rre.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rre.repositories.NotificacionRepository

class NotificationsViewModelFactory(
    private val notificacionRepository: NotificacionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(notificacionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
