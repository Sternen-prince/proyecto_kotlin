package com.example.rre.repositories

import androidx.lifecycle.LiveData
import com.example.rre.room.dao.PublicacionDao
import com.example.rre.room.entities.PublicacionEntity
import java.text.SimpleDateFormat
import java.util.*

class PublicacionRepository(private val publicacionDao: PublicacionDao) {

    // Obtener todas las publicaciones
    fun obtenerTodasLasPublicaciones(): LiveData<List<PublicacionEntity>> {
        return publicacionDao.getAllPublicaciones()
    }

    // Obtener publicaciones por autor
    fun obtenerPublicacionesPorAutor(autor: String): LiveData<List<PublicacionEntity>> {
        return publicacionDao.getPublicacionesPorAutor(autor)
    }

    // Crear nueva publicación
    suspend fun crearPublicacion(
        titulo: String,
        tipoAviso: String,
        descripcion: String,
        lugar: String,
        imagenUri: String,
        autor: String
    ): Long {
        val fecha = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
        
        val publicacion = PublicacionEntity(
            titulo = titulo,
            tipoAviso = tipoAviso,
            descripcion = descripcion,
            lugar = lugar,
            imagenUri = imagenUri,
            autor = autor,
            fecha = fecha
        )
        
        return publicacionDao.insertPublicacion(publicacion)
    }

    // Obtener publicación por ID
    suspend fun obtenerPublicacionPorId(id: Int): PublicacionEntity? {
        return publicacionDao.getPublicacionById(id)
    }

    // Actualizar publicación
    suspend fun actualizarPublicacion(publicacion: PublicacionEntity) {
        publicacionDao.updatePublicacion(publicacion)
    }

    // Eliminar publicación
    suspend fun eliminarPublicacion(publicacion: PublicacionEntity) {
        publicacionDao.deletePublicacion(publicacion)
    }

    // Eliminar publicación por ID
    suspend fun eliminarPublicacionPorId(id: Int) {
        publicacionDao.deletePublicacionById(id)
    }

    // Contar publicaciones
    suspend fun contarPublicaciones(): Int {
        return publicacionDao.contarPublicaciones()
    }
}
