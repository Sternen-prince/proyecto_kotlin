package com.example.rre.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val nombre: String,
    val rut: String,
    val correo: String,
    val telefono: String,
    val contrasena: String,
    val rol: String
)