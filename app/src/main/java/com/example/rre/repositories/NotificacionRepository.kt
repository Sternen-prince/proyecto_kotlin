package com.example.rre.repositories

import androidx.lifecycle.LiveData
import com.example.rre.room.daos.NotificacionDao
import com.example.rre.room.entities.NotificacionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificacionRepository(private val notificacionDao: NotificacionDao) {

    // Obtener todas las notificaciones
    fun obtenerTodasLasNotificaciones(): LiveData<List<NotificacionEntity>> {
        return notificacionDao.getAllNotificaciones()
    }

    // Obtener notificaciones no leídas
    fun obtenerNotificacionesNoLeidas(): LiveData<List<NotificacionEntity>> {
        return notificacionDao.getNotificacionesNoLeidas()
    }

    // Crear notificación cuando se hace una nueva publicación
    suspend fun crearNotificacionNuevaPublicacion(autorPublicacion: String, tituloPublicacion: String) {
        val mensaje = "$autorPublicacion ha publicado un objeto: $tituloPublicacion"
        val fecha = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
        
        val notificacion = NotificacionEntity(
            mensaje = mensaje,
            tipoNotificacion = "NUEVA_PUBLICACION",
            autorPublicacion = autorPublicacion,
            tituloPublicacion = tituloPublicacion,
            fecha = fecha,
            leida = false
        )
        
        // Solo guardar en base de datos (aparecerá en la pestaña de notificaciones)
        notificacionDao.insertNotificacion(notificacion)
    }

    // Método para crear notificación de prueba
    suspend fun crearNotificacionPrueba() {
        try {
            val mensaje = "Usuario de Prueba ha publicado un objeto: Objeto de prueba"
            val fecha = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
            
            val notificacion = NotificacionEntity(
                mensaje = mensaje,
                tipoNotificacion = "PRUEBA",
                autorPublicacion = "Usuario de Prueba",
                tituloPublicacion = "Objeto de prueba",
                fecha = fecha,
                leida = false
            )
            
            android.util.Log.d("NotificacionRepository", "Creando notificación: $mensaje")
            notificacionDao.insertNotificacion(notificacion)
            android.util.Log.d("NotificacionRepository", "Notificación insertada exitosamente")
        } catch (e: Exception) {
            android.util.Log.e("NotificacionRepository", "Error insertando notificación", e)
            throw e
        }
    }

    // Marcar notificación como leída
    suspend fun marcarComoLeida(id: Int) {
        notificacionDao.marcarComoLeida(id)
    }

    // Eliminar notificación
    suspend fun eliminarNotificacion(id: Int) {
        notificacionDao.deleteNotificacion(id)
    }

    // Contar notificaciones no leídas
    suspend fun contarNotificacionesNoLeidas(): Int {
        return notificacionDao.contarNotificacionesNoLeidas()
    }

    // Actualizar notificación
    suspend fun actualizarNotificacion(notificacion: NotificacionEntity) {
        notificacionDao.updateNotificacion(notificacion)
    }
}
