package com.example.rre.repositories

import com.example.rre.room.daos.UsuarioDao
import com.example.rre.room.entities.UsuarioEntity

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    // Todos los usuarios (para test o mostrar en listas)
    val usuarios = usuarioDao.getAll()

    // Login
    suspend fun login(correo: String, contrasena: String): UsuarioEntity? {
        return usuarioDao.login(correo, contrasena)
    }

    // Insertar usuario
    suspend fun insertar(usuario: UsuarioEntity) {
        usuarioDao.insertUser(usuario)
    }

    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity? {
        return usuarioDao.findByCorreo(correo)
    }

    suspend fun obtenerPorRut(rut: String): UsuarioEntity? {
        return usuarioDao.findByRut(rut)
    }

    // Eliminar usuario
    suspend fun eliminar(usuario: UsuarioEntity) {
        usuarioDao.deleteUser(usuario)
    }

    // Buscar por ID
    suspend fun obtenerPorId(id: Int): UsuarioEntity? {
        return usuarioDao.findById(id)
    }

    // Buscar por nombre
    suspend fun buscarPorNombre(nombre: String): List<UsuarioEntity> {
        return usuarioDao.findByNombre(nombre)
    }

    // Actualizar usuario
    suspend fun actualizar(usuario: UsuarioEntity) {
        usuarioDao.updateUser(usuario)
    }
}
