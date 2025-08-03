package com.example.rre.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "publicaciones")
data class PublicacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val tipoAviso: String,
    val descripcion: String,
    val lugar: String,
    val imagenUri: String,
    val autor: String,
    val fecha: String
)
