package com.example.rre.ui.nueva_publicacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rre.repositories.NotificacionRepository
import com.example.rre.repositories.PublicacionRepository

class PublicacionViewModelFactory(
    private val notificacionRepository: NotificacionRepository,
    private val publicacionRepository: PublicacionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PublicacionViewModel::class.java)) {
            return PublicacionViewModel(notificacionRepository, publicacionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
