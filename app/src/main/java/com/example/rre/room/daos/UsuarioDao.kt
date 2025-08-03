package com.example.rre.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rre.room.entities.UsuarioEntity

@Dao
interface UsuarioDao {

    // Obtener todos los usuarios
    @Query("SELECT * FROM usuarios ORDER BY userId DESC")
    fun getAll(): LiveData<List<UsuarioEntity>>

    // Buscar por nombre
    @Query("SELECT * FROM usuarios WHERE nombre = :nombre")
    suspend fun findByNombre(nombre: String): List<UsuarioEntity>

    // Login: validar por correo y contraseña
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena")
    suspend fun login(correo: String, contrasena: String): UsuarioEntity?

    // Login: validar por correo y contraseña
    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun findByCorreo(correo: String): UsuarioEntity?

    // Insertar usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(usuario: UsuarioEntity)

    //Buscar por rut
    @Query("SELECT * FROM usuarios WHERE rut = :rut LIMIT 1")
    suspend fun findByRut(rut: String): UsuarioEntity?

    // Eliminar usuario
    @Delete
    suspend fun deleteUser(usuario: UsuarioEntity)

    // Buscar por ID
    @Query("SELECT * FROM usuarios WHERE userId = :id")
    suspend fun findById(id: Int): UsuarioEntity?

    // Actualizar usuario
    @Update
    suspend fun updateUser(usuario: UsuarioEntity)
}
