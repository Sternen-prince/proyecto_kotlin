package com.example.rre.utils

import com.example.rre.repositories.NotificacionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object NotificacionTestData {
    
    fun crearNotificacionesDePrueba(repository: NotificacionRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Crear algunas notificaciones de ejemplo
                repository.crearNotificacionNuevaPublicacion(
                    "María González", 
                    "Perro perdido en Plaza Baquedano"
                )
                
                repository.crearNotificacionNuevaPublicacion(
                    "Carlos Mendoza", 
                    "Llaves encontradas en Universidad"
                )
                
                repository.crearNotificacionNuevaPublicacion(
                    "Ana Torres", 
                    "Celular perdido iPhone 13"
                )
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
