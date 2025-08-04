package com.example.rre.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "publicaciones",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["userId"],
            childColumns = ["autorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["autorId"])]
)
data class PublicacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val tipoAviso: String,
    val descripcion: String,
    val lugar: String,
    val imagenUri: String,
    val autorId: Int,
    val fecha: String
)
