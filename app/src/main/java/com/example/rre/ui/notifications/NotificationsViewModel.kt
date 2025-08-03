package com.example.rre.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rre.repositories.NotificacionRepository
import com.example.rre.room.entities.NotificacionEntity
import kotlinx.coroutines.launch

class NotificationsViewModel(private val notificacionRepository: NotificacionRepository) : ViewModel() {

    // Lista de notificaciones
    val notificaciones: LiveData<List<NotificacionEntity>> = notificacionRepository.obtenerTodasLasNotificaciones()

    // Estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Contador de notificaciones no leídas
    private val _contadorNoLeidas = MutableLiveData<Int>()
    val contadorNoLeidas: LiveData<Int> = _contadorNoLeidas

    init {
        cargarContadorNoLeidas()
    }

    // Marcar notificación como leída
    fun marcarComoLeida(notificacion: NotificacionEntity) {
        if (!notificacion.leida) {
            viewModelScope.launch {
                notificacionRepository.marcarComoLeida(notificacion.id)
                cargarContadorNoLeidas()
            }
        }
    }

    // Eliminar notificación
    fun eliminarNotificacion(notificacion: NotificacionEntity) {
        viewModelScope.launch {
            notificacionRepository.eliminarNotificacion(notificacion.id)
            cargarContadorNoLeidas()
        }
    }

    // Cargar contador de notificaciones no leídas
    private fun cargarContadorNoLeidas() {
        viewModelScope.launch {
            val contador = notificacionRepository.contarNotificacionesNoLeidas()
            _contadorNoLeidas.value = contador
        }
    }

    // Refrescar notificaciones
    fun refrescarNotificaciones() {
        cargarContadorNoLeidas()
    }
}