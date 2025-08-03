package com.example.rre.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "comentarios",
    foreignKeys = [
        ForeignKey(
            entity = NotificacionEntity::class,
            parentColumns = ["id"],
            childColumns = ["notificacionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ComentarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val notificacionId: Int,
    val autorComentario: String,
    val contenidoComentario: String,
    val fechaComentario: String
)
