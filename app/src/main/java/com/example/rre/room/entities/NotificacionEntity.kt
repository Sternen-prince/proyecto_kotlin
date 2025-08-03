package com.example.rre.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notificaciones")
data class NotificacionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mensaje: String,
    val tipoNotificacion: String, // "NUEVA_PUBLICACION"
    val autorPublicacion: String,
    val tituloPublicacion: String,
    val fecha: String,
    val leida: Boolean = false
)
